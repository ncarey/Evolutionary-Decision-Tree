package edu.jhu.nick.cs335.hw4;

import edu.jhu.nick.cs335.hw4.tree.DecisionTreeNode;
import edu.jhu.nick.cs335.hw4.data.Example;
import edu.jhu.nick.cs335.hw4.data.ExampleSet;
import edu.jhu.nick.cs335.hw4.tree.traditional.TraditionalDecisionTreeLearning;
import edu.jhu.nick.cs335.hw4.tree.evolutionary.EvolutionaryDecisionTree;
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

  private static String [] monkDataClasses = {"0", "1"};
  private static String [] monkDataAttributes = {"a1","a2","a3","a4","a5","a6"};

  private static String [] mushroomDataClasses = {"e", "p"};
  private static String [] mushroomDataAttributes = {"cap-shape","cap-surface","cap-color","bruises?","odor",
	"gill-attachment","gill-spacing","gill-size","gill-color","stalk-shape","stalk-root",
	"stalk-surface-above-ring","stalk-surface-below-ring","stalk-color-above-ring",
	"stalk-color-below-ring","veil-type","veil-color","ring-number","ring-type","spore-print-color",
	"population","habitat"};

  private static String [] spliceDataClasses = {"N","EI","IE"};
  private static String [] spliceDataAttributes = {"-30","-29","-28","-27","-26","-25","-24","-23","-22","-21","-20",
	"-19","-18","-17","-16","-15","-14","-13","-12","-11","-10","-9","-8","-7","-6","-5","-4","-3","-2","-1",
	"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23",
	"24","25","26","27","28","29","30"};

  /**
   * This method will read in the splice data that
   * MUST BE LOCATED IN <PROJECTHOME>/data/splice/splice.data
   * It will return an ExampleSet representing the entire dataset
   */
  public static ArrayList<Example> readSpliceData(String projectHome) {
 
    ArrayList<Example> examples = new ArrayList<Example>();;
   
    try{
      
      FileInputStream fstream = new FileInputStream(projectHome + "/data/splice/splice.data");
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String strLine;
      
      while ((strLine = br.readLine()) != null)   {
        //System.out.println(strLine);
        String [] splitLine = strLine.split(",");   
        if(splitLine.length != 3) {
          throw new Exception("line of input contained incorrect amount of data: " + splitLine.length + 
		" elements opposed to " + 3 + " elements required");
        }
        String [] attribs = splitLine[2].trim().split("");
        if(attribs.length - 1 != spliceDataAttributes.length){
          throw new Exception("line of input contained incorrect amount of data: " + (attribs.length - 1) + 
		" elements opposed to " + spliceDataAttributes.length + " elements required");
        }

        Example ex = new Example(splitLine[0].trim());
        for(int i = 1; i < attribs.length; i++){
          //System.out.println(spliceDataAttributes[i] + " " + attribs[i]);
          ex.addAttributeValuePair(spliceDataAttributes[i-1], attribs[i]);
        }
        examples.add(ex);
      }
  
      in.close();
    } catch (Exception e) {
      System.err.println("I/O Error: " + e.getMessage());
    }

    return examples;
  }
  /**
   * This method will read in the mushroom data that
   * MUST BE LOCATED IN <PROJECTHOME>/data/mushroom/agaricus-lepiota.data
   * It will return an ExampleSet representing the entire dataset
   */
  public static ArrayList<Example> readMushroomData(String projectHome) {
 
    ArrayList<Example> examples = new ArrayList<Example>();;
   
    try{
      
      FileInputStream fstream = new FileInputStream(projectHome + "/data/mushroom/agaricus-lepiota.data");
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String strLine;
      
      while ((strLine = br.readLine()) != null)   {
        //System.out.println(strLine);
        String [] splitLine = strLine.split(",");   
        if(splitLine.length != (mushroomDataAttributes.length + 1)) {
          throw new Exception("line of input contained incorrect amount of data: " + splitLine.length + 
		" elements opposed to " + (1 + mushroomDataAttributes.length) + " elements required");
        }
        Example ex = new Example(splitLine[0]);
        for(int i = 1; i < splitLine.length; i++){
          ex.addAttributeValuePair(mushroomDataAttributes[i-1], splitLine[i]);
        }
        examples.add(ex);
      }
  
      in.close();
    } catch (Exception e) {
      System.err.println("I/O Error: " + e.getMessage());
    }

    return examples;
  }

  /**
   * This method will read in the MONK data that
   * MUST BE LOCATED IN <PROJECTHOME>/data/MONK/monks-X<suffix>
   * It will return an ExampleSet representing the entire dataset
   */
  public static ArrayList<Example> readMonkData(String projectHome, int monkID, String suffix) {
 
    ArrayList<Example> examples = new ArrayList<Example>();;
   
    try{
      
      FileInputStream fstream = new FileInputStream(projectHome + "/data/MONK/monks-" + monkID + suffix);
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String strLine;
      
      while ((strLine = br.readLine()) != null)   {
        //System.out.println(strLine);
        String [] splitLine = strLine.split("\\s+");   
        if(splitLine.length - 1 != (monkDataAttributes.length + 2)) {
          throw new Exception("line of input contained incorrect amount of data: " + (splitLine.length - 1) + 
		" elements opposed to " + (2 + monkDataAttributes.length) + " elements required");
        }
        Example ex = new Example(splitLine[1]);
        //dont want that last element
        for(int i = 2; i < splitLine.length - 1; i++){
          ex.addAttributeValuePair(monkDataAttributes[i-2], splitLine[i]);
        }
        examples.add(ex);
      }
  
      in.close();
    } catch (Exception e) {
      System.err.println("I/O Error: " + e.getMessage());
    }

    return examples;
  }
  /**
   * This method will read in the congressional voting data that
   * MUST BE LOCATED IN <PROJECTHOME>/data/CongressionalVoting/house-votes-84.data
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

  private static void outputStatistics(DecisionTreeNode root, String [] classes, ArrayList<Example> testExamples, List<Example> trainExamples, long duration) {

    System.out.println("Trainging took " + ((double)duration / 1000000000.0) + " seconds");
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

  private static void outputStatistics2(DecisionTreeNode root, String [] classes, ArrayList<Example> testExamples, List<Example> trainExamples, long duration) {

    System.out.print("" + ((double)duration / 1000000000.0) + ",");
    //System.out.println("Test Set Evaluation:");
    double avgA = 0.0;
    double avgP = 0.0;
    double avgR = 0.0;
    for(int j = 0; j < classes.length; j++){
      String curClass = classes[j];
      //System.out.println("\tStatistics for class " + curClass);
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
      avgA += accuracy;
      avgP += precision;
      avgR += recall;
     // System.out.println("\t\t Accuracy: " + accuracy);   
     // System.out.println("\t\t Precision: " + precision);     
     // System.out.println("\t\t Recall: " + recall); 
    }
    avgA = avgA / classes.length;
    avgP = avgP / classes.length;
    avgR = avgR / classes.length;
    System.out.print("" + avgA + ",");   
    System.out.print("" + avgP + ",");     
    System.out.println("" + avgR); 
    

  }


  public static void main(String[] args) {
    
    String projectHome = System.getenv("DECISION_TREE_HOME");
    int splitFactor = 5;
  
    String dataset = "";
    String useRatio = "";
    String algorithm = "";  

    try{
      dataset = args[0];
      useRatio = args[1];
      algorithm = args[2];

    }catch(Exception e){
      System.out.println("Incorrect usage. Arguments should be <congress|monk1|monk2|monk3|mushroom|splice> <gain|gainRatio> ");
      return;
    }

    ArrayList<String> attributes = new ArrayList<String>();
    String [] classes = {};
    ArrayList<Example> testExamples = new ArrayList<Example>();
    ExampleSet trainExamples = new ExampleSet();
    
    TraditionalDecisionTreeLearning decision = null;

    if(useRatio.equals("gain")){
      decision = new TraditionalDecisionTreeLearning(false);
    }else if(useRatio.equals("gainRatio")){
      decision = new TraditionalDecisionTreeLearning(true);
    }else{
      System.out.println("Incorrect usage. Arguments should be <congress|monk1|monk2|monk3|mushroom|splice> <gain|gainRatio>");
      return;

    }

    if(dataset.equals("congress")){

      attributes = new ArrayList<String>(Arrays.asList(congressDataAttributes));
      classes = congressDataClasses;

      ArrayList<Example> exampleList = readCongressionalData(projectHome);
      
      //split examples into training and test data
      long seed = System.nanoTime();
      Collections.shuffle(exampleList, new Random(seed));

      for(int i = 0; i < exampleList.size(); i++){
        if(i < exampleList.size() / splitFactor){
          testExamples.add(exampleList.get(i));
        }else{
          trainExamples.addExample(exampleList.get(i));
        }
      }

    }else if(dataset.equals("monk1")){
      attributes = new ArrayList<String>(Arrays.asList(monkDataAttributes));
      classes = monkDataClasses;

      ArrayList<Example> trainExamplesArr = readMonkData(projectHome, 1, ".train");
      testExamples = readMonkData(projectHome, 1, ".test");
      
      for(int i = 0; i < trainExamplesArr.size(); i++){
        trainExamples.addExample(trainExamplesArr.get(i));
      }
      
    
    }else if(dataset.equals("monk2")){
      attributes = new ArrayList<String>(Arrays.asList(monkDataAttributes));
      classes = monkDataClasses;

      ArrayList<Example> trainExamplesArr = readMonkData(projectHome, 2, ".train");
      testExamples = readMonkData(projectHome, 2, ".test");
      
      for(int i = 0; i < trainExamplesArr.size(); i++){
        trainExamples.addExample(trainExamplesArr.get(i));
      }

    }else if(dataset.equals("monk3")){
      attributes = new ArrayList<String>(Arrays.asList(monkDataAttributes));
      classes = monkDataClasses;

      ArrayList<Example> trainExamplesArr = readMonkData(projectHome, 3, ".train");
      testExamples = readMonkData(projectHome, 3, ".test");
      
      for(int i = 0; i < trainExamplesArr.size(); i++){
        trainExamples.addExample(trainExamplesArr.get(i));
      }

    }else if(dataset.equals("mushroom")){
      attributes = new ArrayList<String>(Arrays.asList(mushroomDataAttributes));
      classes = mushroomDataClasses;

      ArrayList<Example> exampleList = readMushroomData(projectHome);
      
      //split examples into training and test data
      long seed = System.nanoTime();
      Collections.shuffle(exampleList, new Random(seed));

      for(int i = 0; i < exampleList.size(); i++){
        if(i < exampleList.size() / splitFactor){
          testExamples.add(exampleList.get(i));
        }else{
          trainExamples.addExample(exampleList.get(i));
        }
      }

    }else if(dataset.equals("splice")){
      attributes = new ArrayList<String>(Arrays.asList(spliceDataAttributes));
      classes = spliceDataClasses;

      ArrayList<Example> exampleList = readSpliceData(projectHome);
      
      //split examples into training and test data
      long seed = System.nanoTime();
      Collections.shuffle(exampleList, new Random(seed));

      for(int i = 0; i < exampleList.size(); i++){
        if(i < exampleList.size() / splitFactor){
          testExamples.add(exampleList.get(i));
        }else{
          trainExamples.addExample(exampleList.get(i));
        }
      }
    }else{

      System.out.println("Incorrect usage. Arguments should be <congress|monk1|monk2|monk3|mushroom|splice> <gain|gainRatio>");
      return;
    }

    long duration = System.nanoTime();
    DecisionTreeNode root;
    if(algorithm.equals("evolutionary")){
      EvolutionaryDecisionTree decTree = new EvolutionaryDecisionTree(trainExamples, classes, (attributes.toArray(new String[attributes.size()])), System.nanoTime());
      root = decTree.geneticTreeLearning();
    }else{
      root = decision.decisionTreeLearning(trainExamples, attributes, trainExamples); 
    }
    duration = System.nanoTime() - duration;
   
    outputStatistics2(root, classes, testExamples, trainExamples.getExamples(), duration);

  }
}
