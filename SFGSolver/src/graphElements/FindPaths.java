package graphElements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FindPaths<T> 
{
    private final Graph<T> graph;
    private List<List<T>> fLoops = null;
    public FindPaths(Graph<T> graph) 
    {
        if (graph == null)
            throw new NullPointerException("The input graph cannot be null.");
        this.graph = graph;
    }

    private void validate (T source, T destination) 
    {
        if (source == null)
            throw new NullPointerException("The source: " + source + " cannot be  null.");
        else if (destination == null)
            throw new NullPointerException("The destination: " + destination + " cannot be  null.");
    }
    
    public List<List<T>> getPaths(T source, T destination) 
    {
        validate(source, destination);
        List<List<T>> paths = new ArrayList<List<T>>();   
        getPaths(source, destination, paths, new LinkedHashSet<T>());
        return (getForwardPaths(source, destination, paths));
    }
    
    public List<List<T>> removeDuplicates(List<List<List<T>>> all)
    {
    	List<List<T>> filtered = new ArrayList<List<T>>();
    	for(int i=0;i<all.size();i++)
    		for(int j=0;j<all.get(i).size();j++)
    			filtered.add(all.get(i).get(j));
    	List<List<T>> filtered_noDuplicates = new ArrayList<List<T>>();
    	ArrayList<Integer> indices_of_duplicates;
    	Map<Integer, List<Integer>> mapped_duplicates = new HashMap<Integer, List<Integer>>();
    	for (int i = 0; i < filtered.size(); i++) {
    		indices_of_duplicates = new ArrayList<Integer>();
    		for (int j = i+1; j < filtered.size(); j++) {
    			if (i != j && equalledLists(filtered.get(i), filtered.get(j))) {
    				indices_of_duplicates.add(j);
    			}
    			
    		}
    		mapped_duplicates.put(i, indices_of_duplicates);
    	}
    	boolean [] marked_nodes = new boolean[filtered.size()];
    	for (int i = 0; i < marked_nodes.length; i++) {marked_nodes[i] = true;}
    	for (int i = 0; i < filtered.size(); i++) {
    		ArrayList<Integer> duplicates_set = (ArrayList<Integer>)mapped_duplicates.get(i);
    		for (int j = 0; j < duplicates_set.size(); j++) {
    			marked_nodes[duplicates_set.get(j)] = false;
    		}
    	}
    	for (int i = 0; i < filtered.size(); i++) {
    		if (marked_nodes[i])
    			filtered_noDuplicates.add(filtered.get(i));
    	}
    	fLoops = filtered_noDuplicates;
    	return filtered_noDuplicates;
    }
    
    
    public Map<List<T>, List<List<T>>> getNonTouchingLoops(List<List<T>> forwardPaths, List<List<T>> loops)
    {
    	Map<List<T>, List<List<T>>> mappedNonTouchingLoops = new HashMap<List<T>, List<List<T>>>();
    	List<List<T>> nonTouchingLoops;
    	for(int i=0;i<forwardPaths.size();i++)
    	{
    		nonTouchingLoops = new ArrayList<List<T>>();
    		for(int j=0;j<loops.size();j++)
    			if(!intersects(forwardPaths.get(i), loops.get(j)))
    				nonTouchingLoops.add(new ArrayList<T>(loops.get(j)));
    		mappedNonTouchingLoops.put(forwardPaths.get(i), nonTouchingLoops);
    	}
    	return mappedNonTouchingLoops;
    }
    
    private boolean intersects(List<T> l_1, List<T> l_2)
    {
    	for(int i=0;i<l_1.size();i++)
    		if(l_2.contains(l_1.get(i)))
    			return true;
    	for(int i=0;i<l_2.size();i++)
    		if(l_1.contains(l_2.get(i)))
    			return true;    	
    	return false;
    }
    
    private boolean equalledLists(List<T> l_1, List<T> l_2)
    {
    	boolean isEqualled = true;
    	for(int i=0;i<l_1.size();i++) {
    		if(!l_2.contains(l_1.get(i)))
    			isEqualled = false;
    	}
    	if(isEqualled)
    	{
    		for(int i=0;i<l_2.size();i++) {
    			if(!l_1.contains(l_2.get(i)))
        			isEqualled = false;
    		}
    	}
    	return isEqualled;
    }
    
    public List<List<T>> getLoops()
    {
    	List<List<List<T>>> all = new ArrayList<List<List<T>>>();
    	for (T key : graph.edges.keySet()) 
    	{
    		List<List<T>> loops = new ArrayList<List<T>>();
    		getLoops(key, loops, new ArrayList<T>());
    		List<List<T>> filteredLoops = filterLoops(loops);
    		all.add(filteredLoops);
    	}
    	return removeDuplicates(all);
    }
    
    private boolean areNeighbouredNode(T source, T target)
    {
    	Map<T, Double> neighboured = graph.edgesFrom(source);
    	if(neighboured.containsKey(target))
    		return true;
    	else
    		return false;
    }

    
    private List<List<T>> getForwardPaths(T source, T destination, List<List<T>> paths)
    {
    	List<List<T>> forwardPaths = new ArrayList<List<T>>();
    	for(int i=0;i<paths.size();i++)
    		if(paths.get(i).get(0).equals(source)&&paths.get(i).get(paths.get(i).size()-1).equals(destination)
				&&foundElementTwice(paths.get(i)))
    			forwardPaths.add(paths.get(i));
    	return forwardPaths;
    }
    
    private void getPaths (T current, T destination, List<List<T>> paths, LinkedHashSet<T> path) 
    {
        path.add(current);
        if (current == destination) 
        {
            paths.add(new ArrayList<T>(path));
            path.remove(current);
            return;
        }
        final Set<T> edges  = graph.edgesFrom(current).keySet();
        for (T t : edges) 
        {
        	if (!path.contains(t))
                getPaths (t, destination, paths, path);
        }
        path.remove(current);
    }
    
    private List<List<T>> filterLoops(List<List<T>> loops)
    {
    	List<List<T>> filteredLoops = new ArrayList<List<T>>();
    	
    	for(int i=0;i<loops.size();i++)
    	{
    		boolean allNeighboured = true;
    		if(loops.get(i).get(0).equals(loops.get(i).get(loops.get(i).size()-1)))
    		{
    			for(int j=1;j<loops.get(i).size();j++)
        		{
        			if(!areNeighbouredNode(loops.get(i).get(j-1), loops.get(i).get(j)))
        				allNeighboured = false;
        		}
    			if(allNeighboured)
    	    		filteredLoops.add(loops.get(i));
    		}
    	}
    	return filteredLoops;
    }
    
    private void getLoops(T current, List<List<T>> loops, List<T> loop)
    {
    	if(loop.contains(current))
    	{
    		loop.add(current);
    		loops.add(new ArrayList<T>(loop));
    		loop.remove(current);
    		return;
    	}
    	loop.add(current);
    	final Set<T> edges  = graph.edgesFrom(current).keySet();
    	for (T t : edges) {
    		if (!loops.contains(t)) {
    			getLoops(t, loops, loop);
    		}
    	}
        loop.remove(current);
    }
    
    private boolean foundElementTwice(List<T> p)
    {
    	for(int i=1;i<p.size();i++)
    		if((p.get(i).toString().charAt(0))==(p.get(i-1).toString().charAt(0)))
    				return false;
    	return true;
    }
    
    private String bitprint(int u) {
		String s = "";
		for (int n = 0; u > 0; ++n, u >>= 1)
			if ((u & 1) > 0)
				s += n + " ";
		return s;
	}

	private int bitcount(int u) {
		int n;
		for (n = 0; u > 0; ++n, u &= (u - 1))
			;// Turn the last set bit to a 0
		return n;
	}

	private ArrayList<String> comb(int c, int n) {
		ArrayList<String> s = new ArrayList<String>();
		for (int u = 0; u < 1 << n; u++)
			if (bitcount(u) == c)
				s.add(bitprint(u));
		Collections.sort(s);
		return s;
	}
	
	public List<List<List<List<T>>>> calcNonTouching () {
		
		List<List<List<List<T>>>> all = new ArrayList<List<List<List<T>>>>();
		ArrayList<List<List<T>>> individuals = new ArrayList<List<List<T>>>();
		
		for (int i = 0; i < fLoops.size(); i++) { // add individual loops, each in a container
			List<List<T>> single_container = new ArrayList<List<T>>();
			single_container.add(fLoops.get(i));
			individuals.add(single_container);
		}
		all.add(individuals);
		int order = 2;
		
		while(true) { // for each possible order
			ArrayList<String> all_combinations = this.comb(order, fLoops.size());
			List<List<List<T>>> collection = new ArrayList<List<List<T>>>();
			for (int j = 0; j < all_combinations.size(); j++) {
				List<List<T>> single_collection = new ArrayList<List<T>>();
				String [] each_combination = all_combinations.get(j).split(" ");
				if (!touching(each_combination)) {
					for (int k = 0; k < each_combination.length; k++) {
						single_collection.add(fLoops.get(Integer.parseInt(each_combination[k])));
					}
				}
				if (!single_collection.isEmpty()) // this combination is not valid
					collection.add(single_collection);
			}
			if (collection.isEmpty()) break; // last iteration did not yield any collection
			else all.add(collection);
			order++;
		}
		return all;
	}

	private boolean touching(String[] each_combination) {
		// TODO Auto-generated method stub
		for (int i = 0; i < each_combination.length; i++) {
			for (int j = i+1; j < each_combination.length; j++) {
				if (this.intersects(fLoops.get(Integer.parseInt(each_combination[i])), 
						fLoops.get(Integer.parseInt(each_combination[j])))) {
					return true;
				}
			}
		}
		return false;
	}
}