package tools;

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
