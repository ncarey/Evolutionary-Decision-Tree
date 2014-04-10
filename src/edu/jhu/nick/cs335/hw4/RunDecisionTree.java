package edu.jhu.nick.cs335.hw4;

import edu.jhu.nick.cs335.hw4.tree.DecisionTreeNode;
import edu.jhu.nick.cs335.hw4.data.Example;
import edu.jhu.nick.cs335.hw4.data.ExampleSet;
import edu.jhu.nick.cs335.hw4.tree.traditional.TraditionalDecisionTreeLearning;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
/**
 * Driver class for the DecisionTree classification program.
 * @author Nick Carey
 */
public class RunDecisionTree {

  private static String [] congressDataClasses = {"republican", "democrat"};
  private static String [] congressDataAttributes = {"handicapped-infants", "water-project-cost-sharing",
	"adoption-of-the-budget-resolution", "physician-fee-freeze", "el-salvador-aid", "religious-groups-in-schools",
	"anti-satellite-test-ban", "aid-to-nicaraguan-contras", "mx-missle", "immigration", 
	"synfuels-corporation-cutback", "education-spending", "superfund-right-to-sue", "crime", 
	"duty-free-exports", "export-administration-act-south-africa" };

  /**
   * This method will read in the congressional voting data that
   * MUST BE LOCATED IN <PROJECTHOME>/data/CongressionalVoting/house-votes-84.names
   * It will return an ExampleSet representing the entire dataset
   */
  public static ArrayList<Example> readCongressionalData(String projectHome) {
 
    ArrayList<Example> examples = new ArrayList<Example>();;
   
    try{
      
      FileInputStream fstream = new FileInputStream(projectHome + "/data/CongressionalVoting/house-votes-84.data");
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String strLine;
      
      while ((strLine = br.readLine()) != null)   {
        //System.out.println(strLine);
        String [] splitLine = strLine.split(",");   
        if(splitLine.length != (congressDataAttributes.length + 1)) {
          throw new Exception("line of input contained incorrect amount of data: " + splitLine.length + 
		" elements opposed to " + (1 + congressDataAttributes.length) + " elements required");
        }
        Example ex = new Example(splitLine[0]);
        for(int i = 1; i < splitLine.length; i++){
          ex.addAttributeValuePair(congressDataAttributes[i-1], splitLine[i]);
        }
        examples.add(ex);
      }
  
      in.close();
    } catch (Exception e) {
      System.err.println("I/O Error: " + e.getMessage());
    }

    return examples;
  }

  private static void outputStatistics(DecisionTreeNode root, String [] classes, ArrayList<Example> testExamples, List<Example> trainExamples) {

    System.out.println("Test Set Evaluation:");
    for(int j = 0; j < classes.length; j++){
      String curClass = classes[j];
      System.out.println("\tStatistics for class " + curClass);
      int truePos = 0;
      int trueNeg = 0;
      int falsePos = 0;
      int falseNeg = 0;
      for(int i = 0; i < testExamples.size(); i++){
        String actualClass = testExamples.get(i).getClassification();
        String foundClass = root.classify(testExamples.get(i));
   
        //todo account for multivariate classifications
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
      System.out.println("\t\t Accuracy: " + accuracy);   
      System.out.println("\t\t Precision: " + precision);     
      System.out.println("\t\t Recall: " + recall); 

    }
    System.out.println("Train Set Evaluation:");
    for(int j = 0; j < classes.length; j++){
      String curClass = classes[j];
      System.out.println("\tStatistics for class " + curClass);
      int truePos = 0;
      int trueNeg = 0;
      int falsePos = 0;
      int falseNeg = 0;
      for(int i = 0; i < trainExamples.size(); i++){
        String actualClass = trainExamples.get(i).getClassification();
        String foundClass = root.classify(trainExamples.get(i));
   
        //todo account for multivariate classifications
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
            falsePos++;
          }
        }
      }
      int total = truePos + trueNeg + falsePos + falseNeg;
      double accuracy = ((double)(trueNeg + truePos)) / (double)total;
      double precision = ((double)truePos) / (double)(truePos + falsePos);
      double recall = ((double)truePos) / (double)(truePos + falseNeg);
      System.out.println("\t\t Accuracy: " + accuracy);   
      System.out.println("\t\t Precision: " + precision);     
      System.out.println("\t\t Recall: " + recall); 

    }

  }


  public static void main(String[] args) {
    
    String projectHome = System.getenv("DECISION_TREE_HOME");
    int splitFactor = 5;
    
    ArrayList<Example> exampleList = readCongressionalData(projectHome);
    //split examples into training and test data
    long seed = System.nanoTime();
    Collections.shuffle(exampleList, new Random(seed));
    ExampleSet trainExamples = new ExampleSet();
    ArrayList<Example> testExamples = new ArrayList<Example>();
    for(int i = 0; i < exampleList.size(); i++){
      if(i < exampleList.size() / splitFactor){
        testExamples.add(exampleList.get(i));
      }else{
        trainExamples.addExample(exampleList.get(i));
      }
    }

   
 
    ArrayList<String> attributes = new ArrayList<String>(Arrays.asList(congressDataAttributes));
    TraditionalDecisionTreeLearning decision = new TraditionalDecisionTreeLearning(false);
    DecisionTreeNode root = decision.decisionTreeLearning(trainExamples, attributes, trainExamples); 
    System.out.print(root.printMe(0));   
    
    outputStatistics(root, congressDataClasses, testExamples, trainExamples.getExamples());

    //System.out.println("Entropy: " + examples.entropy());

  

    /*
    ArrayList<Example> exs = readCongressionalData(projectHome);
    for(Example ex : exs) {
      System.out.println(ex.toString());
    }
    */
  }
}
