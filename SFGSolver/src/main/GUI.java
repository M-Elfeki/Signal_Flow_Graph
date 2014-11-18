package main;

import graphElements.CalculateMaisons;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class GUI extends JFrame implements MouseListener
{
	private static final long serialVersionUID = 1L;
	private JButton addNode, removeNode, addEdge, removeEdge, calculate, setBounds;
	private JTextArea description;
	private JRadioButton straight, curved;
	private JPanel controlPanel, drawingPanel, dataPanel, listsPanel;
	private ButtonGroup choice;
	private int x=-1, y=-1, lastX=0, lastY=0;
	public static char[] nodeNames = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
		 'Q', 'R', 'S', 'T', 'U', 'V', 'X', 'Y', 'Z'};
	private static int nodeCounter=0;
	private CalculateMaisons<String> masons = null;
	private JList forwardPaths, correspondingNontouchingLoops, loops, overallNontouchingLoops;
	private String [] forwardPaths_str, correspondingNontouchingLoops_str, loops_str, overallNontouchingLoops_str;
	private boolean addingNode = false, removingNode = false, addingSource = false, removingSource = false, addingTarget = false,
			removingTarget = false, addingStartingPoint = false, addingEndingPoint = false;
	private DrawingArea.Node source = null, target = null;
	private String ip_str="", op_str="";
	private JScrollPane scroll_1, scroll_2, scroll_3, scroll_4;
	
	
	public GUI()
	{
		initializeComponents();
		initializeComponents_2();
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200,680);
		setVisible(true);
		setTitle("SFG Solver");
		add(dataPanel);
		add(controlPanel);
		controlPanel.setLocation(0, 0);
		drawingPanel.setLocation(210, 0);
		getContentPane().setBackground(Color.white);
		add(drawingPanel);
		
		addMouseListener(this);
	}
	
	private void initializeComponents_2() 
	{
		listsPanel = new JPanel(null);
		forwardPaths_str = new String[1000];
		forwardPaths_str[0] = "              Forward Paths";
		correspondingNontouchingLoops_str = new String[1000];
		correspondingNontouchingLoops_str[0] = " Corresponding Non Touching Loops";
		loops_str = new String[1000];
		loops_str[0] = "                        Loops";
		overallNontouchingLoops_str = new String[1000];
		overallNontouchingLoops_str[0] = "    Overall Non Touching Loops";
		forwardPaths = new JList(forwardPaths_str);
		forwardPaths.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		forwardPaths.setLayoutOrientation(JList.VERTICAL);
		forwardPaths.setVisibleRowCount(-1);
		correspondingNontouchingLoops = new JList(correspondingNontouchingLoops_str);
		correspondingNontouchingLoops.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		correspondingNontouchingLoops.setLayoutOrientation(JList.VERTICAL);
		correspondingNontouchingLoops.setVisibleRowCount(-1);
		loops = new JList(loops_str);
		loops.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		loops.setLayoutOrientation(JList.VERTICAL);
		loops.setVisibleRowCount(-1);
		overallNontouchingLoops = new JList(overallNontouchingLoops_str);
		overallNontouchingLoops.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		overallNontouchingLoops.setLayoutOrientation(JList.VERTICAL);
		overallNontouchingLoops.setVisibleRowCount(-1);
		scroll_1 = new JScrollPane(forwardPaths);
		scroll_2 = new JScrollPane(correspondingNontouchingLoops);
		scroll_3 = new JScrollPane(loops);
		scroll_4 = new JScrollPane(overallNontouchingLoops);
		scroll_1.setSize(new Dimension(250, 220));
		scroll_2.setSize(new Dimension(280, 220));
		scroll_3.setSize(new Dimension(250, 220));
		scroll_4.setSize(new Dimension(250, 220));
		scroll_1.setLocation(50, 20);
		scroll_2.setLocation(310, 20);
		scroll_3.setLocation(610, 20);
		scroll_4.setLocation(870, 20);
		forwardPaths.addListSelectionListener(corresponding);
		listsPanel.add(scroll_1);
		listsPanel.add(scroll_2);
		listsPanel.add(scroll_3);
		listsPanel.add(scroll_4);
		dataPanel.add(listsPanel, BorderLayout.CENTER);
	}

	private void initializeComponents() 
	{
		addNode = new JButton("Add Node");
		addNode.setSize(new Dimension(120, 27));
		addNode.setLocation(30, 10);
		addNode.addActionListener(addVertex);
		removeNode= new JButton("Remove Node");
		removeNode.setSize(new Dimension(150, 27));
		removeNode.setLocation(20, 45);
		removeNode.addActionListener(deleteVertex);
		addEdge = new JButton("Add Edge");
		addEdge.setSize(new Dimension(120, 27));
		addEdge.setLocation(30, 115);
		addEdge.addActionListener(newEdge);
		removeEdge = new JButton("Remove Edge");
		removeEdge.setLocation(20, 176);
		removeEdge.setSize(new Dimension(150, 27));
		removeEdge.addActionListener(deleteEdge);
		calculate = new JButton("Calculate!");
		calculate.setLocation(30, 280);
		calculate.setSize(new Dimension(120, 27));
		calculate.setEnabled(false);
		calculate.addActionListener(proceed);
		setBounds = new JButton("Set Bounds");
		setBounds.setLocation(20, 240);
		setBounds.setSize(new Dimension(150, 27));
		setBounds.addActionListener(choose_input);
		straight = new JRadioButton("Straight", true);
		straight.setLocation(10, 145);
		straight.setSize(new Dimension(90, 27));
		curved = new JRadioButton("Curved", false);
		curved.setLocation(107, 145);
		curved.setSize(new Dimension(90, 27));
		choice = new ButtonGroup();
		choice.add(straight);
		choice.add(curved);
		description = new JTextArea();
		description.setEditable(false);
		description.setOpaque(false);
		description.setForeground(Color.RED);
		description.setVisible(false);
		description.setFont(new Font("Arial", 13, 18));
		description.setLocation(10, 330);
		description.setSize(new Dimension(180, 70));
		controlPanel = new JPanel();
		controlPanel.setLayout(null);
		controlPanel.setSize(new Dimension(200, 400));
		drawingPanel = new DrawingArea();
		drawingPanel.setSize(new Dimension(980, 400));
		dataPanel = new JPanel(new BorderLayout());
		dataPanel.setLocation(0, 400);
		dataPanel.setSize(new Dimension(1200, 300));
		controlPanel.add(description);
		controlPanel.add(addNode);
		controlPanel.add(removeNode);
		controlPanel.add(addEdge);
		controlPanel.add(straight);
		controlPanel.add(curved);
		controlPanel.add(removeEdge);
		controlPanel.add(setBounds);
		controlPanel.add(calculate);
		controlPanel.add(calculate);
	}
	
	
	ActionListener addVertex = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			description.setFont(new Font("Arial", 13, 18));
			description.setForeground(Color.red);
			setBounds.setEnabled(true);
			description.setText("Please Allocate the \n Vertex To be added");
			description.setVisible(true);
			addingNode = true; removingNode = false; addingSource = false; removingSource = false; addingTarget = false;
			removingTarget = false; addingStartingPoint = false; addingEndingPoint = false;
		}
	};
	
	ActionListener deleteVertex = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			description.setFont(new Font("Arial", 13, 18));
			setBounds.setEnabled(true);
			description.setForeground(Color.red);
			description.setText("Please Allocate the \nVertex To be Deleted");
			description.setVisible(true);
			addingNode = false; removingNode = true; addingSource = false; removingSource = false; addingTarget = false;
			removingTarget = false; addingStartingPoint = false; addingEndingPoint = false;
		}
	};
	
	ActionListener newEdge = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			description.setFont(new Font("Arial", 13, 18));
			setBounds.setEnabled(true);
			description.setForeground(Color.red);
			description.setText("Please Allocate the \nSource of the Edge");
			description.setVisible(true);
			addingNode = false; removingNode = false; addingSource = true; removingSource = false; addingTarget = false;
			removingTarget = false; addingStartingPoint = false; addingEndingPoint = false;
		}
	};
	
	ActionListener deleteEdge = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			description.setFont(new Font("Arial", 13, 18));
			setBounds.setEnabled(true);
			description.setForeground(Color.red);
			description.setText("Please Allocate the \nSource of the Edge");
			description.setVisible(true);
			addingNode = false; removingNode = false; addingSource = false; removingSource = true; addingTarget = false;
			removingTarget = false; addingStartingPoint = false; addingEndingPoint = false;
		}
	};
	
	ListSelectionListener corresponding = new ListSelectionListener()
	{
		@Override
		public void valueChanged(ListSelectionEvent e) 
		{
			int index = forwardPaths.getSelectedIndex();
			if(index>0)
			{
				if(masons!=null)
				{
					for (List<String> key : masons.nonTouchingLoops.keySet()) 
					{
						if(equalledLists(key, masons.forwardPaths.get(index-1)))
						{
							ArrayList<List<String>> value =(ArrayList<List<String>>) masons.nonTouchingLoops.get(key);
							for(int i=0;i<value.size();i++)
							{
								correspondingNontouchingLoops_str[i+1] = value.get(i).toString();
								correspondingNontouchingLoops.setForeground(Color.darkGray);
							}
							if(value.isEmpty())
							{
								for(int i=1;i<correspondingNontouchingLoops_str.length;i++)
								{
									correspondingNontouchingLoops_str[i] = "";
								}
							}
						}
					}
					correspondingNontouchingLoops.updateUI();
				}
					
			}
		}
	};
	
	private boolean equalledLists(List<String> l_1, List<String> l_2)
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
	
	ActionListener proceed = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			addingNode = false; removingNode = false; addingSource = false; removingSource = false; addingTarget = false;
			removingTarget = false; addingStartingPoint = false; addingEndingPoint = false;
			description.setText("Please Allocate the \nInput Node");
			description.setVisible(true);
			masons = new CalculateMaisons<String>(DrawingArea.graph, ip_str, op_str);
			for (int i = 0; i < masons.forwardPaths.size(); i++) 
			{				
				forwardPaths_str[i+1] = masons.forwardPaths.get(i).toString();
			}
			
			for (int i = 0; i < masons.loops.size(); i++) {
				loops_str[i+1] = masons.loops.get(i).toString();
			}
			int str_cntr = 1;
			for (int i = 0; i < masons.nonTouching.size(); i++) { // container
				overallNontouchingLoops_str[str_cntr] = "";
				for (int j = 0; j < masons.nonTouching.get(i).size(); j++) { // collection
					overallNontouchingLoops_str[str_cntr] +=  masons.nonTouching.get(i).get(j).toString();
				}
			str_cntr ++;
			}
			
			forwardPaths.updateUI();
			correspondingNontouchingLoops.updateUI();
			loops.updateUI();
			overallNontouchingLoops.updateUI();
			description.setFont(new Font("Arial", 10, 14));
			description.setText("Transfer Function = \n"+Double.toString(masons.numerator)+" / "+
					Double.toString(masons.denominator)+" = "+Double.toString(masons.getTransferFunction()));
			description.setForeground(Color.blue);
			setBounds.setEnabled(true);
			calculate.setEnabled(false);
		}
	};
	
	ActionListener choose_input = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			description.setFont(new Font("Arial", 13, 18));
			description.setForeground(Color.red);
			description.setText("Please Allocate the \nInput Node");
			description.setVisible(true);
			addingNode = false; removingNode = false; addingSource = false; removingSource = false; addingTarget = false;
			removingTarget = false; addingStartingPoint = true; addingEndingPoint = false;
			setBounds.setEnabled(false);
		}
	};
	

	@Override
	public void mousePressed(MouseEvent e) 
	{
		x = e.getX()-210;
		y = e.getY()-35;
		if(addingNode&&x!=-1&&y!=-1&&x!=lastX&&y!=lastY)
		{
			((DrawingArea) drawingPanel).addNode((nodeNames[nodeCounter]+""), x,y);
			nodeCounter++;
			setBounds.setEnabled(true);
			setBounds.setEnabled(true);
			addingNode = false;
			description.setVisible(false);
		}
		else if(removingNode&&x!=-1&&y!=-1&&x!=lastX&&y!=lastY)
		{
			((DrawingArea) drawingPanel).removeNode(x, y-20);
			if (((DrawingArea) drawingPanel).getNodes().isEmpty()) 
			{
				setBounds.setEnabled(false);
				setBounds.setEnabled(false);
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
				description.setText("Please Allocate the \nTarget of the Edge");
			}
			else
				description.setVisible(false);
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
				if(straight.isSelected()) {
					((DrawingArea) drawingPanel).addEdge(source, target, weight, drawingPanel);
				}
				else
					((DrawingArea) drawingPanel).addCurvedEdge(source, target, weight, drawingPanel);
			}
			else
				description.setVisible(false);
		}
		else if(removingSource&&x!=-1&&y!=-1&&x!=lastX&&y!=lastY)
		{
			if(((DrawingArea) drawingPanel).getNode(x, y-20)!=null)
			{
				source = ((DrawingArea) drawingPanel).getNode(x, y-20);
				removingSource = false;
				removingTarget = true; 
				description.setText("Please Allocate the \nTarget of the Edge");
			}
			else
				description.setVisible(false);
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
				description.setVisible(false);
		}
		else if(addingStartingPoint&&x!=-1&&y!=-1&&x!=lastX&&y!=lastY)
		{
			if(((DrawingArea) drawingPanel).getNode(x, y-20)!=null)
			{
				source = ((DrawingArea) drawingPanel).getNode(x, y-20);
				description.setText("Please Allocate the \nOutput Node");
				description.setVisible(true);
				addingStartingPoint = false;
				addingEndingPoint = true;
			}
			else
			{
				description.setVisible(false);
				setBounds.setEnabled(true);
			}
		}
		else if(addingEndingPoint&&x!=-1&&y!=-1&&x!=lastX&&y!=lastY)
		{
			if(((DrawingArea) drawingPanel).getNode(x, y-20)!=null)
			{
				description.setVisible(false);
				target = ((DrawingArea) drawingPanel).getNode(x, y-20);
				calculate.setEnabled(true);
				ip_str = source.name;
				op_str = target.name;
				addingEndingPoint = false;
			}
			else
			{
				description.setVisible(false);
				setBounds.setEnabled(true);
			}
		}
		lastX = x;
		lastY = y;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
	
	public static void main(String[] args) 
	{
		new GUI();
	}
}
