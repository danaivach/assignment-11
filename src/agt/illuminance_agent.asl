/*
* The URL of the W3C Web of Things Thing Description of a lab environment
* Simulated lab URL: "https://raw.githubusercontent.com/Interactions-HSG/example-tds/was/tds/interactions-lab.ttl"
* Real lab URL: "https://raw.githubusercontent.com/Interactions-HSG/example-tds/was/tds/interactions-lab.ttl"
*/
learning_lab_environment("https://raw.githubusercontent.com/Interactions-HSG/example-tds/was/tds/interactions-lab.ttl").

!start.

@start
+!start : learning_lab_environment(Url)
<-
  makeArtifact("qlearner", "tools.QLearner", [Url], QLearnerId);
  calculateQ([2,2], 1.0, 0.1, 0.9, 3.0, 10.0).
