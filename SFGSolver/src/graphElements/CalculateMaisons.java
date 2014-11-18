package graphElements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalculateMaisons<T> {
	
	public Graph<String> graph;
	public FindPaths<String> find_paths;
	public List<List<String>> forwardPaths;
	public double transferFunction = 0.0;
	public List<List<String>> loops;
	public Map<List<String>, List<List<String>>> nonTouchingLoops;
	public List<List<List<List<String>>>> nonTouching;
	public Map<List<T>, Double> valuesOfDelta; 
	public double numerator = 0;
	public double denominator = 0;
	
	public double getTransferFunction() {
		return transferFunction;
	}

	public FindPaths<String> getFind_paths() {
		return find_paths;
	}

	public List<List<String>> getForwardPaths() {
		return forwardPaths;
	}

	public List<List<String>> getLoops() {
		return loops;
	}

	public Map<List<String>, List<List<String>>> getNonTouchingLoops() {
		return nonTouchingLoops;
	}

	public List<List<List<List<String>>>> getNonTouching() {
		return nonTouching;
	}
	
	public CalculateMaisons(Graph<String> graph, T source_node, T target_node) 
	{
		this.graph = graph;
		this.find_paths = new FindPaths<String>(graph);
		forwardPaths = find_paths.getPaths((String)source_node, (String)target_node);
		loops = find_paths.getLoops();
		nonTouchingLoops = find_paths.getNonTouchingLoops(forwardPaths, loops);
		nonTouching = find_paths.calcNonTouching();
		transferFunction = calcTransferFunction();
		valuesOfDelta = new HashMap<List<T>, Double>();
	}
	
	private double calculateNumerator() {
		// calculates gains of each forward path;
		double gain=0;
		for (int i = 0; i <forwardPaths.size(); i++) { 
			double single_gain = 0.0;
			single_gain += graph.getWeight(forwardPaths.get(i));
			List<List<String>> corresponding_nontouching_loops = nonTouchingLoops.get(forwardPaths.get(i));
			double loops_gains = 1;
			// calculate gains of corresponding nontouching loops
			for (int j = 0; j < corresponding_nontouching_loops.size(); j++) {
				loops_gains -= graph.getWeight(corresponding_nontouching_loops.get(j));
			}
			
			// calculate sum of product of those gains;
			gain += single_gain*loops_gains;
		}
		numerator = gain;
		return gain;
	}
	
	private double calculateDelta() {
		
		double gain = 1;
		for (int i = 0; i < nonTouching.size(); i++)  {
			double cell_gain = 0;
			for (int j = 0; j < nonTouching.get(i).size(); j++) {
				double container_gain = 1;
				for (int k = 0; k < nonTouching.get(i).get(j).size(); k++) {
					container_gain *= graph.getWeight(nonTouching.get(i).get(j).get(k));
				}
				cell_gain += container_gain;
			}
			gain += Math.pow((-1), i+1)*cell_gain;
		}
		denominator = gain;
		return gain;
	}
	
	public double calcTransferFunction() {
		return (this.calculateNumerator()/this.calculateDelta());
	}
	
}
