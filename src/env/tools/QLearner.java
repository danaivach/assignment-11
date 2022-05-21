package tools;

import com.opencsv.CSVWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.stream.Collectors;
import com.google.common.collect.Lists;
import java.util.*;
import java.util.logging.*;
import cartago.Artifact;
import cartago.OPERATION;
import cartago.OpFeedbackParam;

public class QLearner extends Artifact {

  private Lab lab;
  private int stateCount;
  private int actionCount;
  private HashMap<Integer, double[][]> qTables;

  private static final Logger LOGGER = Logger.getLogger(QLearner.class.getName());

  public void init(String environmentURL) {

    this.lab = new Lab(environmentURL);

    this.stateCount = this.lab.getStateCount();
    LOGGER.info("Initialized with a state space of n="+ stateCount);

    this.actionCount = this.lab.getActionCount();
    LOGGER.info("Initialized with an action space of m="+ actionCount);

    qTables = new HashMap<>();
  }

/**
* Computes a Q matrix for the state space and action space of the lab, and against
* a goal description. For example, the goal description can be of the form [z1level, z2Level],
* where z1Level is the desired value of the light level in Zone 1 of the lab,
* and z2Level is the desired value of the light level in Zone 2 of the lab.
* For exercise 11, the possible goal descriptions are:
* [1,1], [1,2], [1,3], [2,1], [2,2], [2,3], [3,1], [3,2], [3,3].
*
*<p>
* HINT 1: Use the methods of {@link LearningEnvironment} (implemented in {@link Lab})
* to interact with the learning environment (here, the lab), e.g., to retrieve the
* applicable actions, perform an action at the lab etc.
*</p>
*<p>
* HINT 2: Use the method {@link #initializeQTable()} to retrieve an initialized Q matrix.
*</p>
*<p>
* HINT 2: Use the method {@link #initializeQTable()} to retrieve an initialized Q matrix.
*</p>
*<p>
* HINT 3: Use the method {@link #printQTable(double[][])} to print a Q matrix.
*</p>
* @param  goalDescription  the desired goal against the which the Q matrix is calculated (e.g., [2,3])
* @param  episodes the number of episodes used for calculating the Q matrix
* @param  alpha the learning rate with range [0,1].
* @param  gamma the discount factor [0,1]
* @param epsilon the exploration probability [0,1]
* @param reward the reward assigned when reaching the goal state
*/
  @OPERATION
  public void calculateQ(Object[] goalDescription , Object episodes, Object alpha, Object gamma, Object epsilon, Object reward) {

    List<Object> goalStateDescription = new ArrayList<>();
    for (int k=0; k<goalDescription.length ; k++) {
      goalStateDescription.add(Integer.valueOf(goalDescription[k].toString()));
    }

    // Read compatible states
    List<Integer> goalStates = this.lab.getCompatibleStates(goalStateDescription);
    LOGGER.info("Desired states: " + goalStates);

    // Read current state
    int currentState = this.lab.readCurrentState();
    LOGGER.info("Current state: " + currentState);

    int intEpisodes = Integer.valueOf(episodes.toString());
    Double doubleAlpha = Double.valueOf(alpha.toString());
    Double doubleGamma = Double.valueOf(gamma.toString());
    Double doubleEpsilon = Double.valueOf(epsilon.toString());
    int intReward = Integer.valueOf(reward.toString());

    // Init Q matrix
    double[][] qTable = initializeQTable();

    Random rd = new Random();

    int rounds = 0;

    int assignedReward;
    int action;

    for (int e=0; e< intEpisodes; e++) {

      // Randomize initial state
      resetFrom(currentState);

      // Read current state
      currentState = this.lab.readCurrentState();
      LOGGER.info("Starting episode: " + e + ", current state:" + currentState);

      while(!goalStates.contains(currentState)) {

        // Get applicable actions
        List<Integer> applicableActions =  this.lab.getApplicableActions(currentState);
        //LOGGER.info("Applicable actions: " + applicableActions);

        if (rd.nextDouble() < doubleEpsilon){
          int randomIndex = rd.nextInt(applicableActions.size());
          action = applicableActions.get(randomIndex);
        } else {
          action = (int) maxQ(qTable, currentState, applicableActions)[0];
        }

        // Perform action
        this.lab.performAction(action);
        try {
          Thread.sleep(60000);
        } catch (InterruptedException exception) {
          LOGGER.severe(exception.getMessage());
        }
        LOGGER.info("Round: " + ++rounds);

        // Read current state
        currentState = this.lab.readCurrentState();
        try {
          Thread.sleep(10000);
        } catch (InterruptedException exception) {
          LOGGER.severe(exception.getMessage());
        }
        //LOGGER.info("Current state: " + currentState);

        // Assign reward
        if (goalStates.contains(currentState)) {
          assignedReward = intReward;
        } else {
          assignedReward = 0;
        }

        // Update Q matrix
        qTable[currentState][action] = (1-doubleAlpha)*qTable[currentState][action] + doubleAlpha*(assignedReward + (doubleGamma*maxQ(qTable, currentState, applicableActions)[1]));

      }
    }

    LOGGER.info("Q learning is completed");
    printQTable(qTable);
    for (int goalState : goalStates) {
      qTables.put(goalState, qTable);
    }

    // Randomize state
    resetFrom(currentState);
  }

  // SOLUTION - used for debugging
  private void resetFrom(int currentState) {
    List<Integer> actions = this.lab.getApplicableActions(currentState);

    for (int action : actions) {
      this.lab.performAction(action);
      try {
        Thread.sleep(60000);
      } catch (InterruptedException exception) {
        LOGGER.severe(exception.getMessage());
      }
    }
  }

  //SOLUTION
  private double[] maxQ(double[][] qTable, int currentState, List<Integer> applicableActions) {

        double[] actionQPair = new double[2];

        int bestAction = applicableActions.get(0);
        double maxValue = qTable[currentState][bestAction];

        for (int nextAction : applicableActions) {
            double value = qTable[currentState][nextAction];

            if (value > maxValue)
                bestAction = nextAction;
                maxValue = value;
        }

        actionQPair[0] = bestAction;
        actionQPair[1] = maxValue;

        return actionQPair;
    }

    @OPERATION
    public void getActionFromState(Object[] goalDescription, Object[] stateDescription,
      OpFeedbackParam<String> actionTag, OpFeedbackParam<Object[]> payloadTags,
      OpFeedbackParam<Object[]> payload) {

        List<Integer> goalStates = this.lab.getCompatibleStates(Arrays.asList(goalDescription));

        double[][] qTable = qTables.get(goalStates.get(0));

        List<Integer> currentState = this.lab.getCompatibleStates(Arrays.asList(stateDescription));

        List<Integer> applicableActions = this.lab.getApplicableActions(currentState.get(0));

        double[] actionQPair = maxQ(qTable, currentState.get(0), applicableActions);

        Action a = this.lab.getAction((int) actionQPair[0]);

        actionTag.set(a.getActionTag());

        payloadTags.set(a.getPayloadTags());

        payload.set(a.getPayload());
    }


    /**
    * Print the Q matrix
    *
    * @param qTable the Q matrix
    */
  void printQTable(double[][] qTable) {
    System.out.println("Q matrix");
    for (int i = 0; i < qTable.length; i++) {
      System.out.print("From state " + i + ":  ");
     for (int j = 0; j < qTable[i].length; j++) {
      System.out.printf("%6.2f ", (qTable[i][j]));
      }
      System.out.println();
    }

    int len1 = qTable.length;
    int len2 = qTable[0].length;

    // row, col
    List<List<String>> stringTable = new ArrayList<>();

    for (int i=0; i<len1; i++) {
      List<String> row = new ArrayList<>();
      for (int j=0; j<len2; j++) {
        row.add(String.valueOf(qTable[i][j]));
      }
      stringTable.add(row);
    }

    try {

      String csv = "output.csv";
      CSVWriter writer = null;

      writer = new CSVWriter(new FileWriter(csv));

      for(List<String> each: stringTable){
        writer.writeNext(each.toArray(new String[each.size()]));
      }
      writer.close();

    } catch (IOException e) {
      LOGGER.severe(e.getMessage());
    }

  }

  /**
  * Initialize a Q matrix
  *
  * @return the Q matrix
  */
 private double[][] initializeQTable() {
    double[][] qTable = new double[this.stateCount][this.actionCount];
    for (int i = 0; i < stateCount; i++){
      for(int j = 0; j < actionCount; j++){
        qTable[i][j] = 0.0;
      }
    }
    return qTable;
  }
}
