Nick Carey
Artificial Intelligence


Evolutionary-Decision-Tree
==========================

Implementation of a standard decision tree classification algorithm and a special
evolutionary decision tree algorithm. Run on some UCI machine learning datasets.

Note that more details in the readme will come with the writeup. Only details regarding running the program
will appear here right now.  


Usage:
 - From the project home directiory:

    - first run ./compile.sh
 
    - ./run_traditional.sh <congress|monk1|monk2|monk3|mushroom|splice> <gain|gainRatio>
    
    - Example:  ./run_traditional.sh congress gain


    - ./run_evolutionary.sh <congress|monk1|monk2|monk3|mushroom|splice>

    - Example: ./run_evolutionary.sh congress


Choice of test and train set:
  Except for the monk sets, where test and train sets are already created for you,
  I create test/train sets by randomly shuffling the entire set, taking the first fifth
  as the test set and the other four fifths as the train set

My Evolutionary algorithm runs until either accuracy of 80% is reached, or 3000 generations have gone by.
Timing information on runs is located in the output directory, included with sample outputs.

Listing of Files:

/src/edu/jhu/nick/cs335/hw4/RunDecisionTree.java
 - main class, driver for the program.
/src/edu/jhu/nick/cs335/hw4/data/Example.java
 - represents a data entry. One example from the input data
/src/edu/jhu/nick/cs335/hw4/data/ExampleSet.java
 - represents a Set of examples with maintained statistics
/src/edu/jhu/nick/cs335/hw4/tree/DecisionTreeNode.java
 - recursively represents a DecisionTree.
/src/edu/jhu/nick/cs335/hw4/tree/traditional/TraditionalDecisionTreeLearning.java
 - implementation of traditional decision tree learning algorithm
/src/edu/jhu/nick/cs335/hw4/tree/evolutionary/EvolutionaryDecisionTree.java
 - implementation of evolutionary decision tree algorithm


Reflections:
  I am pretty disappointed with this assignment. Again, I spent 70-80% of my time implementing
  infrastructure, IO and other non-AI related programming.  I worked on this assignment at least 6 hours a
  day for an entire week, and I felt most of my time was spent performing non-AI related coding.
  Please provide code scaffolding in the future so that we may focus on AI concepts, not Object Oriented design
  or input/output functions. 




