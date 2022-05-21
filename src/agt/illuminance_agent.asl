/*
* The URL of the W3C Web of Things Thing Description of a lab environment
* Simulated lab URL: "https://raw.githubusercontent.com/Interactions-HSG/example-tds/was/tds/interactions-lab.ttl"
* Real lab URL: "https://raw.githubusercontent.com/Interactions-HSG/example-tds/was/tds/interactions-lab-real.ttl"
*/
learning_lab_environment("https://raw.githubusercontent.com/Interactions-HSG/example-tds/was/tds/interactions-lab-real.ttl").
task_requirements([0,0]).

!start.

@start
+!start : learning_lab_environment(Url) & task_requirements(GoalStatus)
<-
  /*
  makeArtifact("lab", "wot.ThingArtifact", [Url], LabId);

  !read_status;

  ?current_status(Status);

  !
  getActionFromState()

  !read_status;

  ?current_status(Status); */

  makeArtifact("qlearner", "tools.QLearner", [Url], QLearnerId);
  calculateQ(GoalStatus, 10, 0.1, 0.7, 0.3, 10.0);

.



  +!read_status : true
<-
  readProperty("https://example.org/was#Status", Types, Values);

  .nth(Z1LevelIndex,Types,"http://example.org/was#Z1Level");
  .nth(Z2LevelIndex,Types,"http://example.org/was#Z2Level");
  .nth(Z1LightIndex,Types,"http://example.org/was#Z1Light");
  .nth(Z2LightIndex,Types,"http://example.org/was#Z2Light");
  .nth(Z1BlindsIndex,Types,"http://example.org/was#Z1Blinds");
  .nth(Z2BlindsIndex,Types,"http://example.org/was#Z2Blinds");
  .nth(SunshineIndex,Types,"http://example.org/was#Sunshine");

  .nth(Z1LevelIndex,Values,Z1Level);
  .nth(Z2LevelIndex,Values,Z2Level);
  .nth(Z1LightIndex,Values,Z1Light);
  .nth(Z2LightIndex,Values,Z2Light);
  .nth(Z1BlindsIndex,Values,Z1Blinds);
  .nth(Z2BlindsIndex,Values,Z2Blinds);
  .nth(SunshineIndex,Values,Sunshine);

  -+current_status([Z1Level, Z2Level, Z1Light, Z2Light, Z1Blinds, Z2Blinds, Sunshine]);
  .

+current_status(Status) : true
  <- .print(Status).
