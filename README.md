# Exercise 11: Reinforcement Learning Agents

A template for an application implemented with the [JaCaMo 0.9](http://jacamo.sourceforge.net/?page_id=40) framework for programming Multi-Agent Systems (MAS).

### Project structure
```bash
├── simulator
│   ├── README.md
│   └── simulator_flow.json # A simulator for the lab environment 
├── src
│   ├── agt
│   │   └── illuminance_agent.asl # The agent responsible for managing the light level in the lab
│   └── env
│       ├── tools
│       │   ├── Action.java 
│       │   ├── Lab.java # Lab instances manage the state space and action space of a lab environment (simulated or real) - extends LearningEnvironment
│       │   ├── LearningEnvironment.java # An abstract class whose concrete classes help in learning environments
│       │   └── QLearner.java # A QLearner artifact for performing Q learning in lab environments
│       └── wot
│           └── ThingArtifact.java #  A thing artifact for enabling the interaction with a Thing based on a W3C Web of Things Thing Description
└── task.jcm
```

### How to set up the simulator
See instructions in [/simulator](/simulator).

### How to run the project
Run with [Gradle 7.4](https://gradle.org/): 
- MacOS and Linux: run the following command
- Windows: replace `./gradlew` with `gradlew.bat`

```shell
./gradlew task
```
