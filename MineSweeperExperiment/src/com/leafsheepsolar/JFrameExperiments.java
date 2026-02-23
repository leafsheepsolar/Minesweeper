package com.leafsheepsolar;

import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class JFrameExperiments extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel nPanel;
	private JPanel cPanel;
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
		nPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		nPanel.setPreferredSize(new Dimension(0,51));//width is ignored, stretched in the NORTH section of border
//		nPanel.setMinimumSize(getMinimumSize());
		contentPane.add(nPanel, BorderLayout.NORTH);
		
		mineLabel = new JLabel("mineLabel");
		mineLabel.setBounds(10, 5, 75, 40);
		mineLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		nPanel.add(mineLabel);
		
		timerLabel = new JLabel("timerLabel");
		timerLabel.setBounds(383, 5, 75, 40);
		timerLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		nPanel.add(timerLabel);
		
		JLabel lbl4 = new JLabel("lbl");
		lbl4.setBounds(5, 30, 140, 25);
		nPanel.add(lbl4);
		
		int rows = 10;
		int cols = 15;
		JButton[][] buttons = new JButton[rows][cols];
		for(int r = 0; r<rows; r++) {
			for(int c = 0; c<cols; c++) {
				JButton button = new JButton();
				button.setPreferredSize(new Dimension(25,25));
				buttons[r][c] = button;
			}
		}
		
		JButton gameIcon = new JButton(":)");
		gameIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createButtonGrid(buttons);
			}//
		});

		gameIcon.setBounds(getWidth()/2 - 20, nPanel.getY()+3, 40, 40);
		nPanel.add(gameIcon);
		
		addComponentListener(new ComponentAdapter() {
		    @Override
		    public void componentResized(ComponentEvent e) {
		    	String str = getSize().toString();
		    	String str1 = str.substring(18);
		    	lbl4.setText(str1);
				gameIcon.setBounds(getWidth()/2 - 30, 5, 40, 40);
				timerLabel.setBounds(getWidth()-105, 5, 70, 40);
		    	
		    }
		});
		
	}
	
	/**
	 * 	<p>grid of Cells with rows and cols
	 * 	<p>cell size is determined by Cell size
	 * 	<p>JPanel size is determined by the grid size
	 */
	public void createButtonGridBagLayout(JButton[][] buttons) {
	    int rows = buttons.length;
	    int cols = buttons[0].length;
	    int buttonSize = (int)buttons[0][0].getPreferredSize().getHeight();

		/* final */ JPanel cPanel = new JPanel(new GridBagLayout());
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
	    cPanel.setPreferredSize(cPanelDim);
	    
	    int frameHeight= cPanelDim.height + (int)nPanel.getPreferredSize().getHeight();//frame size is the nPanel + cPanel sizes added up
	    int frameWidth = cPanelDim.width; //cPanelWidth
	    setBounds(100,100,frameWidth,frameHeight);
		contentPane.add(cPanel, BorderLayout.CENTER);
	}
	
	public void createButtonGrid(JButton[][] buttons) {
		int rows = buttons.length;
	    int cols = buttons[0].length;
	    int buttonSize = (int)buttons[0][0].getPreferredSize().getHeight();

	    
		/* final */JPanel cPanel = new JPanel(new GridLayout(rows,cols));
	    int cPanelWidth = buttonSize*cols;
	    int cPanelHeight = buttonSize*rows;
	    Dimension cPanelDim = new Dimension(cPanelWidth, cPanelHeight);
	    cPanel.setMinimumSize(cPanelDim);
	    
	    int frameHeight= cPanelDim.height + (int)nPanel.getPreferredSize().getHeight();//frame size is the nPanel + cPanel sizes added up
	    int frameWidth = cPanelDim.width; //cPanelWidth
	    setBounds(100,100,frameWidth,frameHeight);

	    for (int r = 0; r < rows; r++) {
	        for (int c = 0; c < cols; c++) {
	            
	            cPanel.add(buttons[r][c]);
	        }
	    }
	    
		contentPane.add(cPanel, BorderLayout.CENTER);
	}
}
