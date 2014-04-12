package edu.jhu.nick.cs335.hw4.tree.evolutionary;
import edu.jhu.nick.cs335.hw4.data.ExampleSet;
import edu.jhu.nick.cs335.hw4.data.Example;
import edu.jhu.nick.cs335.hw4.tree.DecisionTreeNode;
import java.util.Random;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Set;

public class EvolutionaryDecisionTree {

  private ExampleSet examples;
  
  //array contianing possible classes of an example. needed for fitness calculation
  private String [] classes;
  //array contianing possible attributes of an example. needed for random decision tree calculation
  private String [] attributes;
  //Contains a sorted map of fitness values mapped to members of the population
  private TreeMap<Double, ArrayList<DecisionTreeNode>> population;  
  private int generation;
  private Random rand;
  private int popSize = 50;
  private int maxRandTreeDepth;
  private int childPerGen = 25;

  public EvolutionaryDecisionTree(ExampleSet example, String [] classesIn, String [] attrIn, long seed){
    this.classes = classesIn;
    this.attributes = attrIn;
    this.examples = example;
    population = new TreeMap<Double, ArrayList<DecisionTreeNode>>();
    generation = 0;
    rand = new Random(seed);
    //generate starting population
    maxRandTreeDepth = 8;
    System.out.println("Generating random decision trees..");
    for(int i = 0; i < popSize; i++){
      DecisionTreeNode cur = generateRandomDecisionTree(maxRandTreeDepth, 0);
      System.out.println("Generated random decision tree " + i);
      Double fit = new Double(this.fitness(cur));
      if(population.containsKey(fit)){
        population.get(fit).add(cur);
      }else{
        ArrayList<DecisionTreeNode> arr = new ArrayList<DecisionTreeNode>();
        arr.add(cur);
        population.put(fit, arr);
      }
    }
    System.out.println("Finished generating random decision trees");

  }

  /**
   * Method that implements the genetic algorithm - Call this method to get the
   * output of the evolutionarly decision tree algorithm
   *
   */
  public DecisionTreeNode geneticTreeLearning(){
    
    boolean convergenceReached = false;
    while(!convergenceReached){
     
      //System.out.println("Finding reproducers..");
      TreeMap<Double, ArrayList<DecisionTreeNode>> reproducers = this.rankSelection(popSize / 5);
      //System.out.println("Found reproducers");
      for(int i = 0; i < childPerGen; i++){
        int pindex1 = rand.nextInt(reproducers.entrySet().size());
        int pindex2 = rand.nextInt(reproducers.entrySet().size());
        Double[] keyArr = reproducers.keySet().toArray(new Double[reproducers.keySet().size()]);

        

        DecisionTreeNode p1 = reproducers.get(keyArr[pindex1]).get(0);
        DecisionTreeNode p2 = reproducers.get(keyArr[pindex2]).get(0);
        //DecisionTreeNode p2 = reproducers.get(keyArr[pindex2]).get(rand.nextInt(reproducers.get(keyArr[pindex2]).size()));
        //System.out.println("Making child");
        DecisionTreeNode child = this.reproduce(p1, p2);
        if(rand.nextInt(100) == 1){
          //System.out.println("Mutating child");
          child = this.mutate(child);
        }
        //System.out.println("calc child fitnesss");
        double childFitness = this.fitness(child);

       // System.out.println("add child to population");
        if(population.containsKey(childFitness)){
          population.get(childFitness).add(child);
        }else{
          ArrayList<DecisionTreeNode> arr = new ArrayList<DecisionTreeNode>();
          arr.add(child);
          population.put(childFitness, arr);
        }

      }

      //kill the weakest
      
      population = rankSelection(popSize);
      generation++;
      System.out.println("Generation " + generation);
      if(this.fitnessReached(.8) || generationLimitReached(3000)){
        convergenceReached = true;
      }
    }
    return population.pollLastEntry().getValue().get(0);
  }


  private DecisionTreeNode generateRandomDecisionTree(int maxDepth, int curDepth){
//    System.out.println("Max " + maxDepth + " cur " + curDepth);
    int randIndex;
    //decide whether leaf or parent
    if(curDepth >= maxDepth){
      //leaf node. randomly choose a classification
      randIndex = rand.nextInt(classes.length);   
      return new DecisionTreeNode(classes[randIndex], true);
    }
    //probablity that node is a leaf: 60%
    randIndex = rand.nextInt(10);
    if(randIndex < 6){
      //leaf node
      randIndex = rand.nextInt(classes.length);
      return new DecisionTreeNode(classes[randIndex], true);
    }else{
      randIndex = rand.nextInt(attributes.length);
      //attribute node.  Attribute selected already by randIndex
      DecisionTreeNode node = new DecisionTreeNode(attributes[randIndex], false);
      //give node all seen values as branches to children.
      //recurse for each child
      Set<String> vals = examples.getValues(attributes[randIndex]);
      for( String val : vals ) {
        node.addBranch(val, generateRandomDecisionTree(maxDepth, (curDepth+1)));
      }
      //add branch for unseenValue
      node.addBranch("unseenValue", generateRandomDecisionTree(maxDepth, (curDepth+1)));
      return node;
    }
    //return null;
  }

  private boolean generationLimitReached(int limit){
    if(this.generation <= limit){
      return false;
    }else{
      return true;
    }
  }

  //tests if most fit member of population is fit enough
  private boolean fitnessReached(double maxFit){
    if(population.lastKey() >= maxFit){
      return true;
    }else{
      return false;
    }
  }

 
  /**
   * Returns the n most fit members of the population
   *
   * @param count number of members to include
   */
  private TreeMap<Double, ArrayList<DecisionTreeNode>> rankSelection(int count){
    TreeMap<Double, ArrayList<DecisionTreeNode>> ret = new TreeMap<Double, ArrayList<DecisionTreeNode>>();
    Iterator<Double> it = population.descendingKeySet().iterator();
    int index = 0;
    while(it.hasNext() && index < count){
      Double key = it.next();
      ArrayList<DecisionTreeNode> nodes = population.get(key);
      index += nodes.size();
      ret.put(key, nodes);    
    }  
    return ret;
  } 
  private TreeMap<Double, ArrayList<DecisionTreeNode>> fitnessProportionateSelection(int count){
    return null;
  } 

  /**
   * Reproduction method - Child is produced by swapping a random nodes from p1 with
   * a random node from p2 
   */
  private DecisionTreeNode reproduce(DecisionTreeNode p1, DecisionTreeNode p2){
    DecisionTreeNode child = p1.deepCopy();
    //randomly choose node from child to swap
    DecisionTreeNode toSwap = child.getRandNode(rand);
    DecisionTreeNode swapee = p2.getRandNode(rand).deepCopy();
    toSwap.setNode(swapee);     
    
    return child;
  }
  private DecisionTreeNode mutate(DecisionTreeNode m1){
     m1.getRandNode(rand).setNode(this.generateRandomDecisionTree(2, 0));
     return m1;
  }
  /**
   * Fitness function based on correctly classifying instances of the 
   * training set of examples - More specifically, fitness is calculated
   * as a sum of the accuracy, precision, and recall of the classifier 
   */
  private double fitnessComb(DecisionTreeNode root) {
  
    double fit = 0;
    List<Example> exList = examples.getExamples();

    for(int i = 0; i < classes.length; i++){
      String curClass = classes[i];
      int truePos = 0;
      int trueNeg = 0;
      int falsePos = 0;
      int falseNeg = 0;
      for(int j = 0; j < exList.size(); j++){
        String actualClass = exList.get(j).getClassification();
        String foundClass = root.classify(exList.get(j));

        if(actualClass.equals(curClass)){
          if(foundClass.equals(actualClass)){
            truePos++;
          }else{
            falseNeg++;
          }
        }else{
          if(foundClass.equals(actualClass)){
            trueNeg++;
          }else if(foundClass.equals(curClass)){
            falsePos++;
          }else{
            trueNeg++;
          }
        }
      }
      int total = truePos + trueNeg + falsePos + falseNeg;
      double accuracy = ((double)(trueNeg + truePos)) / (double)total;
      double precision = ((double)truePos) / (double)(truePos + falsePos);
      double recall = ((double)truePos) / (double)(truePos + falseNeg);
      fit += accuracy + precision + recall;
    }

    return fit;
  }
 
  //fitness fucntion based on accuracy
  private double fitness(DecisionTreeNode root){
    List<Example> exList = examples.getExamples();
    int found = 0;
    for(int j = 0; j < exList.size(); j++){
      String actualClass = exList.get(j).getClassification();
      String foundClass = root.classify(exList.get(j));

      if(actualClass.equals(foundClass)){
        found++;
      }
    }
    int total = exList.size();
    double accuracy = ((double)found) / (double)total;

    return accuracy;
    
  }




}
