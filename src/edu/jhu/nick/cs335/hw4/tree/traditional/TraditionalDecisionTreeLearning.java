package edu.jhu.nick.cs335.hw4.tree.traditional;

import edu.jhu.nick.cs335.hw4.tree.DecisionTreeNode;
import edu.jhu.nick.cs335.hw4.data.Example;
import edu.jhu.nick.cs335.hw4.data.ExampleSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * This class implements the Decision-Tree-Learning algorithm described
 * on page 702 or the R&N textbook 3rd edition
 * However, most of the functionality is actually located in ExampleSet.java
 * 
 * @author Nick Carey
 */
public class TraditionalDecisionTreeLearning {

  
  public DecisionTreeNode decisionTreeLearning(ExampleSet examples, ArrayList<String> attributes, ExampleSet parentExamples, boolean useRatio) {

    if(examples.getExampleCount() == 0){
      //no examples present, return most likely classification of parentExamples
      String classification = parentExamples.getMostCommonClassification();
      return new DecisionTreeNode(classification, true);
    }else if(examples.uniformClassification()){
      //if all examples classified the same, return classification
      String classification = examples.getMostCommonClassification();
      return new DecisionTreeNode(classification, true);
    }else if(attributes.size() == 0) {
      //if no attributes left to split on, return most likely classification
      String classification = examples.getMostCommonClassification();
      return new DecisionTreeNode(classification, true);
    }else{
      //find attribute with most importance to split on
      int maxIndex = 0;
      double maxImportance = -99999;
      String maxAttribute = "";
      for(int i = 0; i < attributes.size(); i++) {
        double curImportance = 0;
        if(useRatio){
          curImportance = examples.informationGainRatio(attributes.get(i));
        }else{
          curImportance = examples.informationGain(attributes.get(i));
        }
        if(curImportance > maxImportance) {
          maxImportance = curImportance;
          maxIndex = i;
          maxAttribute = attributes.get(i);
        }
      }
      //create tree node with most important attribute 
      DecisionTreeNode tree = new DecisionTreeNode(maxAttribute, false);
      //what to do with values not see? take random branch? create 'other' value?      


      return tree;
    }
  }

  /**
   * Given a set of data examples, outputs the most common classification of these examples
   *
   */
  private String pluralityValue(ArrayList<Example> examples) {
    
    HashMap<String, Integer> counts = new HashMap<String, Integer>();
    for(Example example : examples) {
      String mclass = example.getClassification();
      if(counts.containsKey(mclass)){
        counts.put(mclass, counts.get(mclass) + 1);
      }else{
        counts.put(mclass, new Integer(1));
      }
    }
    
    String maxClass = "None";
    Integer maxCount = new Integer(0);

    for(Map.Entry<String,Integer> entry : counts.entrySet()) {
      if(entry.getValue() > maxCount) {
        maxCount = entry.getValue();
        maxClass = entry.getKey();
      }
    }

    return maxClass;
  }

  


}
