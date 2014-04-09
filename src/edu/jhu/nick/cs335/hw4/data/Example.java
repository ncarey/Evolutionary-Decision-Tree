package edu.jhu.nick.cs335.hw4.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * Class that represents an example of an instance of data that the algorithm uses to construct a 
 * decision tree for - An example consists of a set of attributes and their values along with
 * a classification
 *
 * @author Nick Carey
 */
public class Example {

  private String classification;
  private HashMap<String, String> attributes;


  public Example() {
    classification = "None";
    attributes = new HashMap<String,String>();
  }
  public Example(String mclass) {
    classification = mclass;
    attributes = new HashMap<String,String>();
  }

  public void setClassification(String classificationIn) {
    this.classification = classificationIn;
  }

  public void addAttributeValuePair(String attr, String val) {
    attributes.put(attr, val);
  }

  public String getAttributeValue(String key) {
    return attributes.get(key);
  }

  public String getClassification() {
    return classification;
  }

  public String toString() {
  
    String ret = "Classification: " + this.classification + "\n";
    for (Map.Entry<String, String> entry : attributes.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      ret += "\t" + key + ": " + value + "\n";
    }
    return ret;

  }

}
