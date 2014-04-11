package edu.jhu.nick.cs335.hw4.tree.traditional;

import edu.jhu.nick.cs335.hw4.tree.DecisionTreeNode;
import edu.jhu.nick.cs335.hw4.data.Example;
import edu.jhu.nick.cs335.hw4.data.ExampleSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.lang.Double;
/**
 * This class implements the Decision-Tree-Learning algorithm described
 * on page 702 or the R&N textbook 3rd edition
 * However, most of the functionality is actually located in ExampleSet.java
 * 
 * @author Nick Carey
 */
public class TraditionalDecisionTreeLearning {

  private boolean useRatio;

  public TraditionalDecisionTreeLearning(boolean useGainRatio) {
    useRatio = useGainRatio;
  }
  
  public DecisionTreeNode decisionTreeLearning(ExampleSet examples, ArrayList<String> attributes, ExampleSet parentExamples) {

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
      double maxImportance = -Double.MAX_VALUE;
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
        //System.out.println(attributes.get(i) + " has importance " + curImportance);        
      }
      attributes.remove(maxIndex);
  
      //System.out.println("Attribute " + maxAttribute + " selected with importance " + maxImportance);       

 
      //create tree node with most important attribute 
      DecisionTreeNode tree = new DecisionTreeNode(maxAttribute, false);
      //what to do with values not seen? take random branch? create 'other' value?      
      //we will create an 'other' branch that points to the value branch with greatest example cardinality
      int maxVal = 0;
      DecisionTreeNode otherBranch = null;
      for(String val : examples.getValues(maxAttribute)) {
        ExampleSet exs = examples.filter(maxAttribute, val);
        int curVal = exs.getExampleCount();
        DecisionTreeNode subtree = decisionTreeLearning(exs, attributes, examples);
        tree.addBranch(val, subtree);
   
        if(curVal > maxVal){
          maxVal = curVal;
          otherBranch = subtree;
        }
      }
      tree.addBranch("unseenValue", otherBranch);
 

      return tree; 
    }
  }
}
