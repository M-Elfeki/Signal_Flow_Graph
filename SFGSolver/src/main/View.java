package main;


import graphElements.CalculateMaisons;
import graphElements.FindPaths;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ListSelectionModel;

public class View extends JFrame implements MouseListener
{
	private static final long serialVersionUID = 1L;
	private JList forwardPaths, correspondingNontouchingLoops, loops, overallNontouchingLoops;
	private String [] forwardPaths_str, correspondingNontouchingLoops_str, loops_str, overallNontouchingLoops_str;
	private JLabel transferFunction;
	private JButton addNode, removeNode, addEdge, removeEdge, calculate, choose_input_btn, choose_output_btn;
	private JLabel description;
	private JRadioButton straight, curved;
	private JPanel controlPanel, drawingPanel, nodesPanel, edgesPanel, proceedPanel, dataPanel;
	private ButtonGroup choice;
	private int x=-1, y=-1, lastX=x, lastY=y;
	public static char[] nodeNames = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
		 'Q', 'R', 'S', 'T', 'U', 'V', 'X', 'Y', 'Z'};
	private static int nodeCounter=0;
	private boolean addingNode = false, addingSource = false, removingNode = false, addingTarget = false, removingSource = false
			, removingTarget = false;
	private DrawingArea.Node source= null, target = null;
	private CalculateMaisons<String> masons;
	private FindPaths<String> findPaths = new FindPaths<String>(DrawingArea.graph);
	private DrawingArea.Node input_node = null, output_node = null;
	private String ip_string = null;
	private String op_string = null;
	private boolean input_chosen = false, output_chosen = false, addingInput = false, addingOutput = false;
	
	public View()
	{
		setLayout(new BorderLayout());
		initializeComponents();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200,700);
		setVisible(true);
		setTitle("SFG Solver");
		add(controlPanel, BorderLayout.NORTH);
		add(drawingPanel, BorderLayout.CENTER);
		//add(dataPanel, BorderLayout.SOUTH);
		addMouseListener(this);
	}
	
	private void initializeComponents()
	{
		addNode = new JButton("Add Node");
		addNode.addActionListener(addVertex);
		removeNode= new JButton("Remove Node");
		removeNode.addActionListener(deleteVertex);
		addEdge = new JButton("Add Edge");
		addEdge.addActionListener(newEdge);
		removeEdge = new JButton("Remove Edge");
		removeEdge.addActionListener(deleteEdge);
		calculate = new JButton("Calculate!");
		calculate.setEnabled(false);
		calculate.addActionListener(proceed);
		choose_input_btn = new JButton("Input");
		choose_input_btn.setEnabled(false);
		choose_input_btn.addActionListener(choose_input);
		choose_output_btn = new JButton("Output");
		choose_output_btn.setEnabled(false);
		choose_output_btn.addActionListener(choose_output);
		straight = new JRadioButton("Straight", true);
		curved = new JRadioButton("Curved", false);
		choice = new ButtonGroup();
		choice.add(straight);
		choice.add(curved);
		description = new JLabel("<<Tranfer Function goes here>>");
		description.setVisible(false);
		controlPanel = new JPanel(new FlowLayout());
		controlPanel.setSize(new Dimension(400, 700));
		nodesPanel = new JPanel(new BorderLayout());
		edgesPanel = new JPanel(new BorderLayout());
		proceedPanel = new JPanel(new FlowLayout());
		dataPanel = new JPanel(new FlowLayout());
		dataPanel.setSize(new Dimension(100, 100));
		dataPanel.setBackground(Color.blue);
		drawingPanel = new DrawingArea();
		forwardPaths_str = new String[1000];
		correspondingNontouchingLoops_str = new String[1000];
		loops_str = new String[1000];
		overallNontouchingLoops_str = new String[1000];
		forwardPaths = new JList(forwardPaths_str);
		forwardPaths.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		forwardPaths.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		forwardPaths.setVisibleRowCount(-1);
		correspondingNontouchingLoops = new JList(correspondingNontouchingLoops_str);
		correspondingNontouchingLoops.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		correspondingNontouchingLoops.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		correspondingNontouchingLoops.setVisibleRowCount(-1);
		loops = new JList(loops_str);
		loops.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		loops.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		loops.setVisibleRowCount(-1);
		overallNontouchingLoops = new JList(overallNontouchingLoops_str);
		overallNontouchingLoops.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		overallNontouchingLoops.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		overallNontouchingLoops.setVisibleRowCount(-1);
		transferFunction = new JLabel();
		nodesPanel.add(description, BorderLayout.NORTH);
		nodesPanel.add(addNode, BorderLayout.CENTER);
		nodesPanel.add(removeNode, BorderLayout.SOUTH);
		nodesPanel.add(choose_input_btn, BorderLayout.WEST);
		nodesPanel.add(choose_output_btn, BorderLayout.EAST);
		edgesPanel.add(addEdge, BorderLayout.NORTH);
		edgesPanel.add(straight, BorderLayout.WEST);
		edgesPanel.add(curved, BorderLayout.EAST);
		edgesPanel.add(removeEdge, BorderLayout.SOUTH);
		proceedPanel.add(calculate);
		controlPanel.add(nodesPanel);
		controlPanel.add(edgesPanel);
		controlPanel.add(proceedPanel);
		dataPanel.add(transferFunction);
		dataPanel.add(forwardPaths);
		dataPanel.add(correspondingNontouchingLoops);
		dataPanel.add(loops);
		dataPanel.add(overallNontouchingLoops);
		
	}
	
	ActionListener addVertex = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			description.setText("Please Allocate the Vertex To be added");
			description.setVisible(true);
			addingNode = true;
		}
	};
	
	ActionListener deleteVertex = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			description.setText("Please Allocate the Vertex To be Deleted");
			description.setVisible(true);
			removingNode = true;
		}
	};
	
	ActionListener newEdge = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			description.setText("Please Allocate the Source of the Edge");
			description.setVisible(true);
			addingSource = true;
		}
	};
	
	ActionListener deleteEdge = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			description.setText("Please Allocate the Source of the Edge");
			description.setVisible(true);
			removingSource = true;
		}
	};
	
	ActionListener proceed = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			masons = new CalculateMaisons<String>(DrawingArea.graph, ip_string, op_string);
//			masons.setFind_paths(findPaths);
//			masons.setForwardPaths(findPaths.getPaths(ip_string, op_string));
//	        masons.setLoops(findPaths.getLoops());
//	        masons.setNonTouchingLoops(findPaths.getNonTouchingLoops(masons.getForwardPaths(), masons.getLoops()));
//	        masons.setNonTouching(findPaths.calcNonTouching());
//			masons.setTransferFunction();
			forwardPaths_str = (String[])masons.getForwardPaths().toArray();
			for (List<String> key : masons.getNonTouchingLoops().keySet()) {
				for (int i = 0; i < masons.getForwardPaths().size(); i++) {
					if (key.equals(masons.getForwardPaths().get(i)))
						correspondingNontouchingLoops_str[i] = masons.getForwardPaths().get(i).toString();
				}
			}
			for (int i = 0; i < masons.getLoops().size(); i++) {
				loops_str[i] = masons.getLoops().get(i).toString();
			}
			for (int i = 0; i < masons.getNonTouching().size(); i++) {
				for (int j = 0; j < masons.getNonTouching().get(i).size(); j++) {
					overallNontouchingLoops_str[i] = masons.getNonTouching().get(i).get(j).toString();
				}
			}
			forwardPaths.updateUI();
			correspondingNontouchingLoops.updateUI();
			loops.updateUI();
			overallNontouchingLoops.updateUI();
			transferFunction.setText(Double.toString(masons.getTransferFunction()));
		}
	};
	
	ActionListener choose_input = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			description.setText("Choose Input Node");
			description.setVisible(true);
			addingInput = true;
		}
	};
	
	ActionListener choose_output = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			description.setText("Choose Output Node");
			description.setVisible(true);
			addingOutput = true;
		}
	};
	
	public static void main(String[] args) {
		new View();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		x = e.getX();
		y = e.getY()-80;
		if(addingNode&&x!=-1&&y!=-1&&x!=lastX&&y!=lastY)
		{
			((DrawingArea) drawingPanel).addNode((nodeNames[nodeCounter]+""), x,y);
			nodeCounter++;
			choose_input_btn.setEnabled(true);
			choose_output_btn.setEnabled(true);
			addingNode = false;
			description.setVisible(false);
		}
		else if(removingNode&&x!=-1&&y!=-1&&x!=lastX&&y!=lastY)
		{
			((DrawingArea) drawingPanel).removeNode(x, y-20);
			if (((DrawingArea) drawingPanel).getNodes().isEmpty()) {
				choose_input_btn.setEnabled(false);
				choose_output_btn.setEnabled(false);
				input_chosen = false;
				output_chosen = false;
				calculate.setEnabled(false);
			}
			removingNode = false;
			description.setVisible(false);
		}
		else if(addingSource&&x!=-1&&y!=-1&&x!=lastX&&y!=lastY)
		{
			if(((DrawingArea) drawingPanel).getNode(x, y-20)!=null)
			{
				source = ((DrawingArea) drawingPanel).getNode(x, y-20);
				addingSource = false;
				addingTarget = true; 
				description.setText("Please Allocate the Target of the Edge");
			}
			else
			{
				description.setVisible(false);
			}
		}
		else if(addingTarget&&x!=-1&&y!=-1&&x!=lastX&&y!=lastY)
		{
			if(((DrawingArea) drawingPanel).getNode(x, y-20)!=null)
			{
				
				target = ((DrawingArea) drawingPanel).getNode(x, y-20);
				addingTarget = false; 
				description.setVisible(false);
				double weight = 0;
				try{weight = Double.parseDouble(JOptionPane.showInputDialog("Please Enter the Weight of this Edge"));}catch(Exception ex){}
				if(straight.isSelected())
					((DrawingArea) drawingPanel).addEdge(source, target, weight, drawingPanel);
				else
					((DrawingArea) drawingPanel).addCurvedEdge(source, target, weight, drawingPanel);
			}
			else
			{
				description.setVisible(false);
			}
		}
		else if(removingSource&&x!=-1&&y!=-1&&x!=lastX&&y!=lastY)
		{
			if(((DrawingArea) drawingPanel).getNode(x, y-20)!=null)
			{
				source = ((DrawingArea) drawingPanel).getNode(x, y-20);
				removingSource = false;
				removingTarget = true; 
				description.setText("Please Allocate the Target of the Edge");
			}
			else
			{
				description.setVisible(false);
			}
		}
		else if(removingTarget&&x!=-1&&y!=-1&&x!=lastX&&y!=lastY)
		{
			if(((DrawingArea) drawingPanel).getNode(x, y-20)!=null)
			{
				
				target = ((DrawingArea) drawingPanel).getNode(x, y-20);
				addingTarget = false; 
				description.setVisible(false);
				((DrawingArea) drawingPanel).removeEdge(source, target, drawingPanel);
			}
			else
			{
				description.setVisible(false);
			}
		}
		
		else if(addingInput&&x!=-1&&y!=-1&&x!=lastX&&y!=lastY)
		{
			if(((DrawingArea) drawingPanel).getNode(x, y-20)!=null)
			{
				
				input_node = ((DrawingArea) drawingPanel).getNode(x, y-20);
				addingInput = false; 
				description.setVisible(false);
				ip_string = input_node.name;
				input_chosen = true;
				if (output_chosen && input_chosen) calculate.setEnabled(true);
				System.out.println(input_chosen);
			}
			else
			{
				description.setVisible(false);
			}
		}
		
		else if(addingOutput&&x!=-1&&y!=-1&&x!=lastX&&y!=lastY)
		{
			if(((DrawingArea) drawingPanel).getNode(x, y-20)!=null)
			{
				
				output_node = ((DrawingArea) drawingPanel).getNode(x, y-20);
				addingOutput = false; 
				description.setVisible(false);
				op_string = output_node.name;
				output_chosen = true;
				if (output_chosen && input_chosen) calculate.setEnabled(true);
				System.out.println(output_chosen);
			}
			else
			{
				description.setVisible(false);
			}
		}
		
		lastX = x;
		lastY = y;
	}

}
