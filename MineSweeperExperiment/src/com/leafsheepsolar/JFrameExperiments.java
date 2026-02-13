package com.leafsheepsolar;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class JFrameExperiments extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel nPanel;
	private JPanel cPanel;
	private GridBagConstraints c;
	private JLabel mineLabel;
	private JLabel timerLabel;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrameExperiments frame = new JFrameExperiments();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JFrameExperiments() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		setBounds(100, 100, 500, 600);
		contentPane = new JPanel(new BorderLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		nPanel = new JPanel(null);
		nPanel.setPreferredSize(new Dimension(0,51));//width is ignored, stretched in the NORTH section of border
		contentPane.add(nPanel, BorderLayout.NORTH);
		
		mineLabel = new JLabel("mineLabel");
		mineLabel.setBounds(10, 3, 75, 40);
		nPanel.add(mineLabel);
		
		timerLabel = new JLabel("timerLabel");
		timerLabel.setBounds(383, 11, 70, 28);
		nPanel.add(timerLabel);
		
		int rows = 15;
		int cols = 10;
		JButton[][] buttons = new JButton[rows][cols];
		for(int r = 0; r<rows; r++) {
			for(int c = 0; c<cols; c++) {
				String str = ""+r+","+c;
				JButton button = new JButton(str);
				button.setPreferredSize(new Dimension(25,25));
				buttons[r][c] = button;
			}
		}
		
		JButton gameIcon = new JButton("reset");
		gameIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createButtonGrid(buttons);
			}
		});
		gameIcon.setBounds(getWidth()/2 - 45, nPanel.getY()+3, 75, 45);
		nPanel.add(gameIcon);
	}
	
	/**
	 * 	<p>grid of Cells with rows and cols
	 * 	<p>cell size is determined by Cell size
	 * 	<p>JPanel size is determined by the grid size
	 */
	public void createButtonGrid(JButton[][] buttons) {
	    int rows = buttons.length;
	    int cols = buttons[0].length;
	    int buttonSize = (int)buttons[0][0].getPreferredSize().getHeight();

	    JPanel cPanel = new JPanel(new GridBagLayout());
	    GridBagConstraints gbc = new GridBagConstraints();

	    gbc.fill = GridBagConstraints.BOTH;
	    gbc.weightx = 1.0;
	    gbc.weighty = 1.0;

	    for (int r = 0; r < rows; r++) {
	        for (int c = 0; c < cols; c++) {
	            gbc.gridx = c; // column
	            gbc.gridy = r; // row
	            
	            cPanel.add(buttons[r][c], gbc);
	        }
	    }
	    
	    int cPanelWidth = buttonSize*cols;
	    int cPanelHeight = buttonSize*rows;
	    Dimension cPanelDim = new Dimension(cPanelWidth, cPanelHeight);
	    
	    int frameHeight= cPanelDim.height + (int)nPanel.getPreferredSize().getHeight();//frame size is the nPanel + cPanel sizes added up
	    int frameWidth = cPanelDim.width; //cPanelWidth
	    Dimension frameDim = new Dimension(frameWidth, frameHeight);
	    setPreferredSize(frameDim);
		/*
		* 
		*
		*/
		contentPane.add(cPanel, BorderLayout.CENTER);
	}
	
	
}
