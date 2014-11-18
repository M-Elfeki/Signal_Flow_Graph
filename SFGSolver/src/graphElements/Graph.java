package graphElements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class Graph<T> 
{
    private final Map<T, Map<T, Double>> graph = new HashMap<T, Map<T, Double>>();
    public Map<T, ArrayList<T>> edges = new HashMap<T, ArrayList<T>>();
    public List<T> edgedNodes = new ArrayList<T>();
    public boolean addNode(T node) 
    {
        if (node == null) 
        	throw new NullPointerException("The input node cannot be null.");
        else if (graph.containsKey(node)) 
        	return false;
        graph.put(node, new HashMap<T, Double>());
        return true;
    }
    
    public boolean removeNode(T node)
    {
    	 if (node == null) 
         	throw new NullPointerException("The input node cannot be null.");
         else if (!graph.containsKey(node)) 
         	return false;
    	 graph.remove(node);
    	 return true;
    }

    
    public void addEdge (T source, T destination, double weight) 
    {
        if (source == null || destination == null) 
        	throw new NullPointerException("Source and Destination, both should be non-null.");
        else if (!graph.containsKey(source) || !graph.containsKey(destination)) 
            throw new NoSuchElementException("Source and Destination, both should be part of graph");
        graph.get(source).put(destination, weight);
        edgedNodes.add(source);
        edgedNodes.add(destination);
        if(edges.containsKey(source))
        {
        	ArrayList<T> l =edges.get(source);
        	l.add(destination);
        }
        else
        {
        	ArrayList<T> l =new ArrayList<T>();
        	l.add(destination);
        	edges.put(source, l);
        }
    }
    
    
    private double getSingleEdgedWeight(T source, T destination)
    {
    	if (source == null || destination == null) 
        	throw new NullPointerException("Source and Destination, both should be non-null.");
    	else if (!graph.containsKey(source) || !graph.containsKey(destination)) 
            throw new NoSuchElementException("Source and Destination, both should be part of graph");
        return graph.get(source).get(destination);
    }
    
    public double getWeight(List<T> path)
    {
    	double weight=1;
    	for(int i=0;i<path.size()-1;i++)
    		weight*=getSingleEdgedWeight(path.get(i), path.get(i+1));
    	return weight;
    }

    public void removeEdge (T source, T destination) 
    {
        if (source == null || destination == null) 
        	throw new NullPointerException("Source and Destination, both should be non-null.");
        else if (!graph.containsKey(source) || !graph.containsKey(destination))
            throw new NoSuchElementException("Source and Destination, both should be part of graph");
        graph.get(source).remove(destination);
        edgedNodes.remove(source);
        edgedNodes.remove(destination);
        if(edges.get(source).size()==1)
        	edges.remove(source);
        else
        {
        	ArrayList<T> l = edges.get(source);
        	int i=0;
        	for(i=0;i<l.size();i++)
        		if(destination==l.get(i))
        			break;
        	l.remove(i);
        }
    }
    
    public Map<T, Double> edgesFrom(T node) 
    {
        if (node == null)
            throw new NullPointerException("The node should not be null.");
        Map<T, Double> edges = graph.get(node);
        if (edges == null)
            throw new NoSuchElementException("Source node does not exist.");
        return Collections.unmodifiableMap(edges);
    }
}


