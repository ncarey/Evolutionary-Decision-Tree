package edu.jhu.nick.cs335.hw4.tree;

import java.util.ArrayList;
import java.util.HashMap;

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

  public void addBranch(String value, DecisionTreeNode child) {
    children.put(value, child);
  }


}
