package edu.jhu.nick.cs335.hw4.data;

import edu.jhu.nick.cs335.hw4.data.Example;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/**
 * Class that represents a set of data examples - maintains statistics
 * on data examples to reduce complexity of calculating Information Gain
 * and Information Gain Ratio
 * @author Nick Carey
 */
public class ExampleSet {

  // Statistic variable measuring the cardinality of each classification in exampleset
  private HashMap<String, Integer> classificationCounts;

  // Storage variable containing a list of the data examples
  private List<Example> examples;

  /**
   * Statistic variable that maps Attributes->Values->classification->cardinality -
   * In other words, this variable tracks the cardinality of an attribute with a
   * given value and classification - maintaining this statistic variable reduces the
   * complexity off calculating entropy, Information Gain, and IG ratio.
   */
  private HashMap<String, HashMap<String, HashMap<String, Integer>>> attributeStats;

  public ExampleSet() {
    classificationCounts = new HashMap<String, Integer>();
    examples = new ArrayList<Example>();
    attributeStats = new HashMap<String, HashMap<String, HashMap<String, Integer>>>();
  }

  public int getExampleCount(){
    return examples.size();
  }

  //returns true if all example classifications are the same
  public boolean uniformClassification() {
    for(Map.Entry<String, Integer> entry : classificationCounts.entrySet()) {
      if(entry.getValue() == examples.size()){
        return true;
      }
    }
    return false;
  }

  public void addExample(Example ex) {
    //add to examples list
    examples.add(ex);
    //update classification count statistics
    String mclass = ex.getClassification();
    if(classificationCounts.containsKey(mclass)){
      classificationCounts.put(mclass, classificationCounts.get(mclass) + 1);
    }else{
      classificationCounts.put(mclass, new Integer(1));
    }
    //update attribute statistics
    for(Map.Entry<String, String> entry : ex.getAttrEntrySet() ){
      String attr = entry.getKey();
      String val = entry.getValue();

      if(attributeStats.containsKey(attr)){
        if(attributeStats.get(attr).containsKey(val)){
          if(attributeStats.get(attr).get(val).containsKey(mclass)){
            attributeStats.get(attr).get(val).put(mclass, attributeStats.get(attr).get(val).get(mclass)+1);
          }else{
            attributeStats.get(attr).get(val).put(mclass, new Integer(1));
          }
        }else{
          HashMap<String, Integer> classMap = new HashMap<String, Integer>();
          classMap.put(mclass, new Integer(1));

          attributeStats.get(attr).put(val, classMap);
        }
      }else{
        HashMap<String, Integer> classMap = new HashMap<String, Integer>();
        classMap.put(mclass, new Integer(1));
     
        HashMap<String, HashMap<String, Integer>> valMap = new HashMap<String, HashMap<String, Integer>>();        
        valMap.put(val, classMap);

        attributeStats.put(attr, valMap);
      }      
    } 
  }//end of addExample

  /*
   * Returns a deep copy of this ExampleSet but only containing
   * examples with the specified attribute/value pair
   */
  public ExampleSet filter(String attribute, String value) {
    ExampleSet ret = new ExampleSet();
    for(Example ex : examples){
      if(ex.getAttributeValue(attribute).equals(value)){
        ret.addExample(ex);
      }
    }
    return ret;
  }
  
  public String getMostCommonClassification(){
    String maxClass = "N/A";
    Integer maxCount = new Integer(0);
    for(Map.Entry<String,Integer> entry : classificationCounts.entrySet()) {
      if(entry.getValue() > maxCount){
        maxClass = entry.getKey();
        maxCount = entry.getValue();
      }
    }
    return maxClass;
  }

  public double informationGain(String attribute) {
    double entropy = this.entropy();

    double infoGainSum = 0;
    //iterate thru all values for this attribute
    for(Map.Entry<String, HashMap<String,Integer>> valEntry : attributeStats.get(attribute).entrySet()){
      //find cardinality of  all examples with this attribute value and divide by number of examples
      int numExamples = examples.size();
      int numExamplesWithValue = 0;

      for(Map.Entry<String, Integer> entry : valEntry.getValue().entrySet()) {
        numExamplesWithValue += entry.getValue();
      }
      //also find entropy of set of examples with value
      double valEntropy = 0.0;
      for(Map.Entry<String, Integer> entry : valEntry.getValue().entrySet()) {
        double Px = ((double)entry.getValue()) / ((double)numExamplesWithValue);
        valEntropy += Px * (Math.log(Px) / Math.log(2));
      }
      valEntropy = valEntropy * -1;
  
      infoGainSum += (((double)numExamplesWithValue) / (double)numExamples) * valEntropy;

    }
  
    return (entropy - infoGainSum);
  }

  public double entropy(){
    double sum = 0;
    for(Map.Entry<String,Integer> entry : classificationCounts.entrySet()) {
      double Px = ((double)entry.getValue()) / ((double)examples.size());
      sum +=  Px * (Math.log(Px) / Math.log(2));
    }
    return -1 * sum;
  }

  public double informationGainRatio(String attribute) {
    
   
    //first, calculate Split Information Value
    double infoValSum = 0.0;
    int numExamples = examples.size();
    for(Map.Entry<String, HashMap<String,Integer>> valEntry : attributeStats.get(attribute).entrySet()){
  
      int numExamplesWithValue = 0;
      for(Map.Entry<String, Integer> entry : valEntry.getValue().entrySet()) {
        numExamplesWithValue += entry.getValue();
      }
      double term = (((double)numExamplesWithValue) / (double)numExamples);
      infoValSum += term * (Math.log(term) / Math.log(2));
    }

    infoValSum = infoValSum * -1;
    
    //next, fetch info gain and return ratio
    double infoGain = this.informationGain(attribute);
    return (infoGain / infoValSum);
  }
}
