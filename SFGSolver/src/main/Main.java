package main;

import graphElements.CalculateMaisons;
import graphElements.Graph;

import java.util.List;
import java.util.Map;

public class Main 
{
	
	public static void main(String[] args) 
	{
        Graph<String> graph = new Graph<String>();
     
//        graph.addNode("A");
//        graph.addNode("B");
//        graph.addNode("C");
//        graph.addNode("D");
//        graph.addNode("E");
//        graph.addNode("F");
//        graph.addNode("G");
//        
//        graph.addEdge("A", "B", 1);
//        graph.addEdge("B", "C", 5);
//        graph.addEdge("C", "D", 10);
//        graph.addEdge("D", "E", 2);
//        graph.addEdge("E", "F", 1);
//        graph.addEdge("B", "G", 10);
//        graph.addEdge("G", "E", 2);
//        graph.addEdge("G", "G", -1);
//        graph.addEdge("E", "B", -1);
//        graph.addEdge("E", "D", -2);
//        graph.addEdge("D", "C", -1);
//        
//        CalculateMaisons<String> c = new CalculateMaisons<String>(graph, "A", "F");
//        System.out.println(c.getTransferFunction());
//        FindPaths<String> findPaths = new FindPaths<String>(graph);
//        List<List<String>> paths = findPaths.getPaths("A", "F");
        

      graph.addNode("A");
      graph.addNode("B");
      graph.addNode("C");
      graph.addNode("D");
      graph.addNode("J");
      graph.addNode("Z");
      graph.addNode("P");
      
      graph.addEdge("P", "P", 1);
      graph.addEdge("A", "P", 1);
      graph.addEdge("P", "D", 1);
      graph.addEdge("Z", "A", 1);
      graph.addEdge("D", "J", 1);
      graph.addEdge("A", "B", 1);
      graph.addEdge("B", "C", 10);
      graph.addEdge("C", "D", 22);
      graph.addEdge("D", "C", 22);
      graph.addEdge("C", "B", 22);
      graph.addEdge("D", "A", 22);
      
      CalculateMaisons<String> c = new CalculateMaisons<String>(graph, "Z", "J");
      System.out.println(c.calcTransferFunction());
      
//      FindPaths<String> findPaths = new FindPaths<String>(graph);
      List<List<String>> paths =  c.getForwardPaths();
        
        
        List<List<String>> loops = c.getLoops();
        Map<List<String>, List<List<String>>> nonTouchingLoops = c.getNonTouchingLoops();
        //List<List<List<List<String>>>> nonTouching = findPaths.calcNonTouching();
        
        System.out.println("---------------------------------");
        for(int i=0;i<paths.size();i++)
        	System.out.println(paths.get(i).toString());
        System.out.println("---------------------------------");
        for(int i=0;i<loops.size();i++)
        	System.out.println(loops.get(i).toString());
        System.out.println("---------------------------------");
        for (Map.Entry<List<String>, List<List<String>>> entry : nonTouchingLoops.entrySet()) 
        {
        	List<String> key = entry.getKey();
        	List<List<String>> value = entry.getValue();
        	System.out.println(key.toString());
        	for(int i=0;i<value.size();i++)
        		System.out.println(value.get(i).toString());
        	System.out.println("*************");
        }
//        System.out.println("+++++++++++++++");
//        
//        for (int i = 0; i < nonTouching.size(); i++) {
//        	for (int j = 0; j < nonTouching.get(i).size(); j++) {
//        		for (int k = 0; k < nonTouching.get(i).get(j).size(); k++) {
//        			System.out.println(nonTouching.get(i).get(j).get(k));
//        			
//        		}
//        		System.out.println("\tend of pair......");
//        	}
//        	System.out.println("\tEND OF PAIRS......");
//        }
    }
}


