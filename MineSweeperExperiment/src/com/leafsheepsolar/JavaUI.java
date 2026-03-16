package com.leafsheepsolar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;

public class JavaUI extends JFrame {

	private JPanel contentPane, nPanel, cPanel;
	private JButton gameIndicator;
	private StopwatchLabel timer;
	private JLabel mineCounter;
	private GameManager manager;
	final int defaultRows = 16, defaultCols = 30, defaultMines = 99; //this preset doesnt seem optimal either.
	private int previousRows, previousCols, previousMines;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JavaUI frame = new JavaUI();
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
	public JavaUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 600);
		setResizable(false);
		contentPane = new JPanel(new BorderLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		nPanel = new JPanel(null);
		nPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		nPanel.setPreferredSize(new Dimension(0,51));//width is ignored, stretched in the NORTH section of border
		contentPane.add(nPanel, BorderLayout.NORTH);
		
		mineCounter = new JLabel();
		mineCounter.setHorizontalAlignment(SwingConstants.TRAILING);
		mineCounter.setFont(new Font("Tahoma", Font.PLAIN, 28));
		mineCounter.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		mineCounter.setBounds(10, 5, 75, 40);
		nPanel.add(mineCounter);
		
		//setting up the timer
		timer = new StopwatchLabel();
		timer.setBackground(new Color(128,128,128));
		timer.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 4));
		timer.setFont(new Font("Monospaced", Font.BOLD, 16));
		timer.setBounds(383, 5, 75, 40);
		nPanel.add(timer);
		//showing 000 on the timer
		timer.startTimer();
		timer.pause();
		
		//set up the gameIndicator
		gameIndicator = new JButton();
		gameIndicator.setIcon(IconRegistry.getScaled("NEUTRAL","GAME_INDICATOR"));
		gameIndicator.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e) {
				resetBoard(); //the dialogue is a popup
			}
		});
		gameIndicator.setBounds(getWidth()/2 - 20, nPanel.getY()+3, 40, 40); //
		nPanel.add(gameIndicator);

		
		//components in nPanel will be resized proportionally
		addComponentListener(new ComponentAdapter() {
		    @Override
		    public void componentResized(ComponentEvent e) {
				gameIndicator.setBounds(getWidth()/2 - 30, 5, 40, 40);
				timer.setBounds(getWidth()-105, 5, 70, 40);
		    	
		    }
		});
		
		manager = new GameManager(defaultRows, defaultCols, defaultMines, this);
		
	}
	
	//this is to reset the game board
		private void resetBoard() {

		    gameIndicator.setIcon(IconRegistry.getScaled("NEUTRAL","GAME_INDICATOR"));
		    timer.reset();

		    // Step 1: Ask if user wants custom values
		    int customChoice = JOptionPane.showConfirmDialog(
		        gameIndicator,
		        "Use custom board values?",
		        "Game Setup",
		        JOptionPane.YES_NO_OPTION
		    );

		    // yes -> go to custom options
		    if (customChoice == JOptionPane.YES_OPTION) {

		        JTextField rowField = new JTextField();
		        JTextField colField = new JTextField();
		        JTextField mineField = new JTextField();

		        Object[] inputs = {
		            "Rows:", rowField,
		            "Columns:", colField,
		            "Mines:", mineField
		        };

		        int inputResult = JOptionPane.showConfirmDialog(
		            gameIndicator,
		            inputs,
		            "Enter Custom Values",
		            JOptionPane.OK_CANCEL_OPTION
		        );

		        if (inputResult == JOptionPane.OK_OPTION) {
		            int rows = Integer.parseInt(rowField.getText());
		            int cols = Integer.parseInt(colField.getText());
		            int mines = Integer.parseInt(mineField.getText());
		            manager = new GameManager(rows, cols, mines, this);
		        }

		        return;
		    }

		    // not custom -> use presets or past choice?
		    int reuseChoice = JOptionPane.showConfirmDialog(
		        gameIndicator,
		        "Use default values?\n(rows: 16, cols: 30, mines: 99)\n\nSelect NO to reuse previous values.",
		        "Game Setup",
		        JOptionPane.YES_NO_OPTION
		    );

		    if (reuseChoice == JOptionPane.YES_OPTION) {
		        manager = new GameManager(defaultRows, defaultCols, defaultMines, this);
		    } else {
		        manager = new GameManager(previousRows, previousCols, previousMines, this);
		    }
		    
		}
	
		public void addBoard(JPanel cPanel) {
		if(this.cPanel == null) {//if there isnt a cPanel, make a default one
			addBoard1(cPanel);
			return;
		}
		contentPane.remove(this.cPanel);
		contentPane.add(cPanel, BorderLayout.CENTER);
	    pack();
	    setMinimumSize(getSize());
	    this.cPanel = cPanel;
	}
	
	
	private void addBoard1(JPanel cPanel) {//makes a default cPanel
		contentPane.add(cPanel, BorderLayout.CENTER);
	    pack();
	    setMinimumSize(getSize());
	    this.cPanel = cPanel;
	}
	
	public JPanel getGameBoard() {
		return this.cPanel;
	}
	
	public void setPreviousSettings(int rows, int cols, int mines) {
		rows = previousRows;
		cols = previousCols;
		mines = previousMines;
	}
	
	public String getMineCounterText() {
		return mineCounter.getText();
	}
	
	public void updateMineCounterText(String text) {
		mineCounter.setText(text);;
	}
	
	public void startTimer() {
		timer.startTimer();
	}
	
	public void gameWon() {
		gameIndicator.setIcon(IconRegistry.getScaled("GAME_WON","GAME_INDICATOR"));
		timer.pause();	
	}
	
	public void gameLost() {
		gameIndicator.setIcon(IconRegistry.getScaled("GAME_LOST","GAME_INDICATOR"));
		timer.pause();
	}
	
	
}