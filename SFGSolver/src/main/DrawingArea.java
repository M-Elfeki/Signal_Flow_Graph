package main;

import graphElements.Graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class DrawingArea extends JPanel 
{
	private static final long serialVersionUID = 1L;
    private final int ARR_SIZE = 4;
	private int width;
    private int height;
    private ArrayList<Node> nodes;
    
    public ArrayList<Node> getNodes() {
		return nodes;
	}

	private ArrayList<edge> edges;
    private ArrayList<edge> curvedEdges;
    private ArrayList<edge> self_loops;
    private ArrayList<Double> weights;
    private static int weights_counter = 0;
    public static Graph<String> graph = new Graph<String>();
    
    
	public DrawingArea()
	{
		setVisible(true);
		setBackground(Color.white);
		setLayout(new FlowLayout());
		nodes = new ArrayList<Node>();
		edges = new ArrayList<edge>();
		curvedEdges = new ArrayList<edge>();
		self_loops = new ArrayList<edge>();
		weights = new ArrayList<Double>();
		width = 30;
		height = 30;
	}
	
	public class Node 
    {
		int x, y;
		String name;
		public Node(){}
		public Node(String myName, int myX, int myY) 
		{
		    x = myX;
		    y = myY;
		    name = myName;
		}
    }
    
	public class edge
    {
		int i,j;
		double w;
		
		public edge(int ii, int jj, double ww) 
		{
		    i = ii;
		    j = jj;
		    w = ww;
		}
    }
    
    public void addNode(String name, int x, int y) 
    {
    	nodes.add(new Node(name,x,y));
    	this.repaint();
    	graph.addNode(name);
    }
    
    public void removeNode(int x, int y)
    {
    	for(int i=0;i<nodes.size();i++)
    	{
    		if((nodes.get(i).x<=x+25&&nodes.get(i).x>=x-25)&&(nodes.get(i).y<=y+25&&nodes.get(i).y>=y-25))
    		{
    			if(!graph.edgedNodes.contains(nodes.get(i).name))
    			{
    				graph.removeNode(nodes.get(i).name);
    				nodes.remove(i);
    				break;
    			}
				
    		}
    	}
    	this.repaint();
    }
    
    public Node getNode(int x, int y)
    {
    	Node t = null;
    	for(int i=0;i<nodes.size();i++)
    	{
    		if((nodes.get(i).x<=x+25&&nodes.get(i).x>=x-25)&&(nodes.get(i).y<=y+25&&nodes.get(i).y>=y-25))
    		{
    			t = nodes.get(i);
    			break;
    		}
    	}
    	return t;
    }
    
    public void addEdge(Node source, Node target, double weight, JPanel panel) 
    {
    	
    	if(!nodes.contains(source)||!nodes.contains(target))
    		try{throw new Exception("NotFoundNodesException");}catch(Exception ex){}
    	
    	else if (source.equals(target)) { // self-loop
    		weights.add(weight);
        	weights_counter++;
    		int i = nodes.indexOf(source);
        	int j = nodes.indexOf(target);
        	self_loops.add(new edge(i,j, weight));
        	graph.addEdge(source.name, target.name, weight);
        	JLabel wght = new JLabel(Double.toString(weight));
        	wght.setLocation(source.x, source.y - 55);
        	wght.setVisible(true);
        	wght.setBackground(Color.RED);
        	panel.add(wght);
        	this.repaint();
    	}
    	
    	else
    	{
    		weights.add(weight);
        	weights_counter++;
    		int i = nodes.indexOf(source);
        	int j = nodes.indexOf(target);
        	edges.add(new edge(i,j, weight));
        	graph.addEdge(source.name, target.name, weight);
        	JLabel wght = new JLabel(Double.toString(weight));
        	wght.setLocation((source.x+target.x)/2, ((source.y+target.y)/2)-10);
        	wght.setVisible(true);
        	wght.setSize(new Dimension(20, 20));
        	panel.add(wght);
        	this.repaint();
    	}
    }
    public void addCurvedEdge(Node source, Node target, double weight, JPanel panel)
    {
    	
    	if(!nodes.contains(source)||!nodes.contains(target))
    		try{throw new Exception("NotFoundNodesException");}catch(Exception ex){}
    	else if (source.equals(target)) { // self-loop
    		weights.add(weight);
        	weights_counter++;
    		int i = nodes.indexOf(source);
        	int j = nodes.indexOf(target);
        	self_loops.add(new edge(i,j, weight));
        	graph.addEdge(source.name, target.name, weight);
        	this.repaint();
    	}
    	else
    	{
    		weights.add(weight);
        	weights_counter++;
    		int i = nodes.indexOf(source);
        	int j = nodes.indexOf(target);
        	curvedEdges.add(new edge(i,j, weight));
        	graph.addEdge(source.name, target.name, weight);
        	this.repaint();
    	}
    }
    
    public void removeEdge(Node source, Node target, JPanel panel)
    {
    	if(!nodes.contains(source)||!nodes.contains(target))
    		try{throw new Exception("NotFoundNodesException");}catch(Exception ex){}
    	else
    	{
    		int i = nodes.indexOf(source);
        	int j = nodes.indexOf(target);
        	int k=0;
        	for(k=0;k<edges.size();k++)
        	{
        		if(edges.get(k).i==i&&edges.get(k).j==j)
        		{
        			edges.remove(k);
        			break;
        		}
        	}
    		weights.remove(weights_counter-1);
    		weights_counter--;
        	for(k=0;k<curvedEdges.size();k++)
        	{
        		if(curvedEdges.get(k).i==i&&curvedEdges.get(k).j==j)
        		{
        			curvedEdges.remove(k);
        			break;
        		}
        	}
        	this.repaint();
        	graph.removeEdge(source.name, target.name);
    	}
    }
    
    void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
        Graphics2D g = (Graphics2D) g1.create();

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[] {len, len-ARR_SIZE-20, len-ARR_SIZE-20, len},
                      new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
    }
    
        
    public void paint(Graphics g) 
    {
    	int temp = weights_counter;
    	weights_counter = 1;
    	super.paintComponent(g);
		FontMetrics f = g.getFontMetrics();
		int nodeHeight = Math.max(height, f.getHeight());
	
		g.setColor(Color.black);
		for (edge e : edges) 
		{
			drawArrow(g, nodes.get(e.i).x, nodes.get(e.i).y,nodes.get(e.j).x, nodes.get(e.j).y);
			 char[] data = new char[10];
				String weight_string = Double.toString(weights.get(weights_counter-1));
				for (int i = 0; i < weight_string.length(); i++) data[i] = weight_string.charAt(i);
				g.drawChars(data, 0, weight_string.length(), (nodes.get(e.i).x+nodes.get(e.j).x)/2, ((nodes.get(e.i).y + nodes.get(e.j).y)/2) - 15);
				weights_counter ++;
		}
		
		for (edge e : self_loops) 
		{
			g.drawOval(nodes.get(e.i).x, nodes.get(e.i).y - 40, 50, 50);
		    g.setColor(Color.black);
		    double dx = nodes.get(e.i).x - nodes.get(e.j).x, dy = nodes.get(e.i).y - nodes.get(e.j).y;
	        @SuppressWarnings("unused")
			int len = (int) Math.sqrt(dx*dx + dy*dy);
		    g.fillRect(nodes.get(e.j).x, nodes.get(e.j).y - 20, 5, 5);
			g.setColor(Color.black);
			char[] data = new char[10];
			String weight_string = Double.toString(weights.get(weights_counter-1));
			for (int i = 0; i < weight_string.length(); i++) data[i] = weight_string.charAt(i);
			g.drawChars(data, 0, weight_string.length(), nodes.get(e.i).x, nodes.get(e.i).y - 45);
			weights_counter ++;
		}
		
		for (edge e : curvedEdges) 
		{
			if (nodes.get(e.i).y >= nodes.get(e.j).y && nodes.get(e.i).x < nodes.get(e.j).x) {
				int height = 2 * (Math.abs(nodes.get(e.i).y-nodes.get(e.j).y));
				int width = 2 * (Math.abs(nodes.get(e.i).x-nodes.get(e.j).x));
				int x = Math.min(nodes.get(e.i).x, nodes.get(e.j).x);
				int y = Math.abs(Math.max(nodes.get(e.i).y, nodes.get(e.j).y) - (height/2));
				g.drawArc(x, y, width, height, 90, 90);
				g.setColor(Color.black);
			    g.fillRect((x + (width/2) - 20), y, 5, 5);
			    g.setColor(Color.black);
			    char[] data = new char[10];
				String weight_string = Double.toString(weights.get(weights_counter-1));
				for (int i = 0; i < weight_string.length(); i++) data[i] = weight_string.charAt(i);
				g.drawChars(data, 0, weight_string.length(), (nodes.get(e.i).x+nodes.get(e.j).x)/2, ((nodes.get(e.i).y + nodes.get(e.j).y)/2) - 45);
			}
			
			else if (nodes.get(e.i).y >= nodes.get(e.j).y && nodes.get(e.i).x >= nodes.get(e.j).x) {
				int height = 2 * (Math.abs(nodes.get(e.i).y-nodes.get(e.j).y));
				int width = 2 * (Math.abs(nodes.get(e.i).x-nodes.get(e.j).x));
				int x = Math.min(nodes.get(e.i).x, nodes.get(e.j).x) - (width/2);
				int y = Math.abs(Math.max(nodes.get(e.i).y, nodes.get(e.j).y) - (height/2));
				g.drawArc(x, y, width, height, 0, 90);
				//g.setColor(Color.red);
			    g.fillRect((x + (width/2) + 15), y, 5, 5);
			    g.setColor(Color.black);
			    char[] data = new char[10];
				String weight_string = Double.toString(weights.get(weights_counter-1));
				for (int i = 0; i < weight_string.length(); i++) data[i] = weight_string.charAt(i);
				g.drawChars(data, 0, weight_string.length(), (nodes.get(e.i).x+nodes.get(e.j).x)/2, ((nodes.get(e.i).y + nodes.get(e.j).y)/2) - 45);
			}
			
			else if (nodes.get(e.i).y < nodes.get(e.j).y && nodes.get(e.i).x < nodes.get(e.j).x) {
				int height = 2 * (Math.abs(nodes.get(e.i).y-nodes.get(e.j).y));
				int width = 2 * (Math.abs(nodes.get(e.i).x-nodes.get(e.j).x));
				int x = Math.min(nodes.get(e.i).x, nodes.get(e.j).x) - (width/2);
				int y = Math.abs(Math.max(nodes.get(e.i).y, nodes.get(e.j).y) - (height/2));
				g.drawArc(x, y, width, height, 0, 90);
				//g.setColor(Color.red);
				g.fillRect(x + width, y + (height/2) - 20, 5, 5);
				g.setColor(Color.black);
				char[] data = new char[10];
				String weight_string = Double.toString(weights.get(weights_counter-1));
				for (int i = 0; i < weight_string.length(); i++) data[i] = weight_string.charAt(i);
				g.drawChars(data, 0, weight_string.length(), (nodes.get(e.i).x+nodes.get(e.j).x)/2, ((nodes.get(e.i).y + nodes.get(e.j).y)/2) - 45);
			}
			
			else {
				int height = 2 * (Math.abs(nodes.get(e.i).y-nodes.get(e.j).y));
				int width = 2 * (Math.abs(nodes.get(e.i).x-nodes.get(e.j).x));
				int x = Math.min(nodes.get(e.i).x, nodes.get(e.j).x);
				int y = Math.abs(Math.max(nodes.get(e.i).y, nodes.get(e.j).y) - (height/2));
				g.drawArc(x, y, width, height, 90, 90);
			    //g.setColor(Color.red);
			    g.fillRect(x, y + (height/2) - 20, 5, 5);
				g.setColor(Color.black);
				 char[] data = new char[10];
					String weight_string = Double.toString(weights.get(weights_counter-1));
					for (int i = 0; i < weight_string.length(); i++) data[i] = weight_string.charAt(i);
					g.drawChars(data, 0, weight_string.length(), (nodes.get(e.i).x+nodes.get(e.j).x)/2, ((nodes.get(e.i).y + nodes.get(e.j).y)/2) - 45);
			}
			weights_counter ++;
		}
	
		for (Node n : nodes) 
		{
		    int nodeWidth = Math.max(width, f.stringWidth(n.name)+width/2);
		    g.setColor(Color.white);
		    g.fillOval(n.x-nodeWidth/2, n.y-nodeHeight/2, 
			       nodeWidth, nodeHeight);
		    g.setColor(Color.black);
		    g.drawOval(n.x-nodeWidth/2, n.y-nodeHeight/2, 
			       nodeWidth, nodeHeight);
		    
		    g.drawString(n.name, n.x-f.stringWidth(n.name)/2,
				 n.y+f.getHeight()/2);
		}
		weights_counter = temp;
    }
}
