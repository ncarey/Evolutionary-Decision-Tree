package edu.jhu.nick.cs335.hw3.tree.traditional;

import edu.jhu.nick.cs335.hw3.tree.DecisionTreeNode;
import edu.jhu.nick.cs335.hw3.data.Example;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * This class implements the Decision-Tree-Learning algorithm described
 * on page 702 or the R&N textbook 3rd edition
 * 
 * @author Nick Carey
 */
public class TraditionalDecisionTreeLearning {

  
  public DecisionTreeNode decisionTreeLearning(ArrayList<Example> examples, ArrayList<String> attributes, ArrayList<Example> parentExamples) {


    return null;
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
