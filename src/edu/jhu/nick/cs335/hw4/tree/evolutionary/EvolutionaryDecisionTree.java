package edu.jhu.nick.cs335.hw4.tree.evolutionary;
import edu.jhu.nick.cs335.hw4.data.ExampleSet;
import edu.jhu.nick.cs335.hw4.tree.DecisionTreeNode;
import java.util.Random;
import java.util.Map;
import java.util.ArrayList;

class EvolutionaryDecisionTree {

  private ExampleSet examples;
  private ArrayList<DecisionTreeNode> population;  


  public EvolutionaryDecisionTree(ExampleSet example){
    this.examples = example;
    population = new ArrayList<DecisionTreeNode>();

  }


  public DecisionTreeNode generateRandomDecisionTree(int maxDepth, int curDepth){

    long seed = System.nanoTime();    

    //randomly choose whether leaf or parent
    //randomly select an attribute for this node    
    //give it all seen values as branches to children.
    //recurse for each child

    return null;
  }

  

  public double fitness(DecisionTreeNode tree) {

    return 0.0;
  }
  public DecisionTreeNode reproduce(DecisionTreeNode p1, DecisionTreeNode p2){
    return null;
  }
  public DecisionTreeNode mutate(DecisionTreeNode m1){

    return null;
  }
}
