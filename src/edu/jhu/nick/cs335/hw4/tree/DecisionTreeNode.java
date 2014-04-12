package edu.jhu.nick.cs335.hw4.tree;

import edu.jhu.nick.cs335.hw4.data.Example;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
/**
 * Class that represents a Decision Tree Node and in turn, an entire Decision Tree
 * Each Decision Tree Node contains a attribute value which is either the resulting classification
 * or the current node's attribute, depending on whether this node is a leaf or not
 * Each Node also contains a Map of Strings to other child nodes - The strings represent 
 * values that the root attribute could take mapped to the subtree associated with that action
 *
 * @author Nick Carey
 */
public class DecisionTreeNode {

  /*
   * Variable determining whether this Decision tree node is simply a leaf node with a classification value
   */
  private boolean isLeaf;
  
  /*
   * Map values belonging to the root attribute to the resulting subtree
   */
  private HashMap<String, DecisionTreeNode> children;
 
  /*
   * This variable contains either the classification value if this is a leaf node
   * or the root attribute of this tree node
   */
  private String attribute;


  public DecisionTreeNode(String attr, boolean leaf) {
    this.attribute = attr;
    this.isLeaf = leaf;
    
    children = new HashMap<String, DecisionTreeNode>();
  }

  public boolean isLeafNode(){
    return isLeaf;
  }
  public String getAttribute(){
    return attribute;
  }
  public HashMap<String, DecisionTreeNode> getChildren(){
    return children;
  }

  public void setNode(DecisionTreeNode other){
    this.isLeaf = other.isLeafNode();
    this.attribute = other.getAttribute();
    this.children = other.getChildren();
  }


  public DecisionTreeNode getRandNode(Random rand){
    if(this.isLeaf){
      return this;
    }else{
      if(rand.nextInt(100) % 4 == 0){
        return this;
      }else{
        //call on a random child          
        // while(true){
        if(children.keySet().size() == 0){
          System.out.println("Error, has no children?");
          return this;
        }else{
          DecisionTreeNode ret = null;        
          for(Map.Entry<String, DecisionTreeNode> entry : children.entrySet()){
            ret = entry.getValue().getRandNode(rand);
            break;
          }
          return ret;
        } 
          /*
          for(Map.Entry<String, DecisionTreeNode> entry : children.entrySet()){
            if(rand.nextInt(12) % 4 == 0){
              return entry.getValue().getRandNode(rand);
            }          
          }*/
        
      }
    }
  }


  public String classify(Example example){
    if(this.isLeaf){
      return attribute;
    }else{
      String exampleVal = example.getAttributeValue(attribute);
      for(Map.Entry<String, DecisionTreeNode> entry : children.entrySet()) {
        if(entry.getKey().equals(exampleVal)){
          return entry.getValue().classify(example);
        }
      }
      return children.get("unseenValue").classify(example);
    }
  }  

  public void addBranch(String value, DecisionTreeNode child) {
    children.put(value, child);
  }

  public DecisionTreeNode deepCopy(){
    if(this.isLeaf){
      return new DecisionTreeNode(this.attribute, this.isLeaf);
    }else{
      DecisionTreeNode ret = new DecisionTreeNode(this.attribute, this.isLeaf);
      for( Map.Entry<String, DecisionTreeNode> entry : this.children.entrySet()){
        ret.addBranch(entry.getKey(), entry.getValue().deepCopy());
      }
      return ret;
    }
  }

  public String printMe(int tabCounter) {
    String out = "";
    String tab = "";
    for(int i = 0; i < tabCounter; i++){
      tab += "    ";
    }
    if(this.isLeaf){
      out = tab + attribute + "\n";
      return out;
    }else{
      out = tab + attribute + "\n";
      for(Map.Entry<String, DecisionTreeNode> entry : children.entrySet()) {
        if(entry.getKey().equals("unseenValue")){
          continue;
        }
        out = out + tab + entry.getKey() + "\n";
        out = out + entry.getValue().printMe(tabCounter + 1);
      }
      return out;
    }
  }

}
