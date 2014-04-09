package edu.jhu.nick.cs335.hw4;

import edu.jhu.nick.cs335.hw4.tree.DecisionTreeNode;
import edu.jhu.nick.cs335.hw4.data.Example;
import edu.jhu.nick.cs335.hw4.tree.traditional.TraditionalDecisionTreeLearning;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.ArrayList;

/**
 * Driver class for the DecisionTree classification program.
 * @author Nick Carey
 */
public class RunDecisionTree {

  private static String [] congressDataAttributes = {"handicapped-infants", "water-project-cost-sharing",
	"adoption-of-the-budget-resolution", "physician-fee-freeze", "el-salvador-aid", "religious-groups-in-schools",
	"anti-satellite-test-ban", "aid-to-nicaraguan-contras", "mx-missle", "immigration", 
	"synfuels-corporation-cutback", "education-spending", "superfund-right-to-sue", "crime", 
	"duty-free-exports", "export-administration-act-south-africa" };

  /**
   * This method will read in the congressional voting data that
   * MUST BE LOCATED IN <PROJECTHOME>/data/CongressionalVoting/house-votes-84.names
   * It will return an ArrayList of data entries
   */
  public static ArrayList<Example> readCongressionalData(String projectHome) {
  
    ArrayList<Example> examples = new ArrayList<Example>();
   
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


  public static void main(String[] args) {
    
    String projectHome = System.getenv("DECISION_TREE_HOME");

    ArrayList<Example> exs = readCongressionalData(projectHome);
    for(Example ex : exs) {
      System.out.println(ex.toString());
    }

  }
}
