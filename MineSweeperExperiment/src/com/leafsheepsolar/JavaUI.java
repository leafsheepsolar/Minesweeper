package com.leafsheepsolar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class JavaUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane, northPane, boardPane;
	private JButton gameIcon;
	private StopwatchLabel timer;	//MOVE METHODS OUTSIDE CONSTRUCTOR
	private GameManager manager;
	final int defaultRows = 16, defaultCols = 30, defaultMines = 99;
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
		contentPane = new JPanel();
		northPane = new JPanel();
		gameIcon = new JButton();
		timer = new StopwatchLabel();
		boardPane = new JPanel();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane.setLayout(new BorderLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		Dimension contentPaneSize = new Dimension();
		
		
		//TODO: resize north panel to be proportional to window size, only length wise though not width
		//sizing and creating top panel
		northPane.setLayout(null);
		northPane.setPreferredSize(new Dimension((int)contentPane.getSize(contentPaneSize).getWidth(), 30)); //northpanel size is realitive to contentPane size
		contentPane.add(northPane, BorderLayout.NORTH);
		
		boardPane.setLayout(new GridBagLayout());//work on the board layout
		contentPane.add(boardPane, BorderLayout.CENTER);
		
		
		//when clicked will trigger a popup sequence
		gameIcon.setIcon(IconRegistry.getScaled("NEUTRAL"));
		gameIcon.addActionListener(new ActionListener(){ 
			
			public void actionPerformed(ActionEvent e) {
				gameIconActionPerformed();
				
			}
		});
		
		//resize such that the location will be proportional to the northPane's size
		gameIcon.setBounds(northPane.getX(), northPane.getY(), 40, 20);
		northPane.add(gameIcon);
		
		
		//uses StopwatchLabel to create a stopwatch
		timer.setBackground(new Color(128,128,128));
		timer.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 4));
		timer.setFont(new Font("Monospaced", Font.BOLD, 16));
		timer.setBounds(10, 5, 60, 30);
		northPane.add(timer);
		//showing 000 on the timer
		timer.startTimer();
		timer.pause();
		
	}
	
	public void startTimer() {
		timer.startTimer();
	}
	
	public void gameWon() {
			
			gameIcon.setIcon(IconRegistry.getScaled("GAME_WON"));
			timer.pause();	
	}
	
	
	public void gameLost() {
			gameIcon.setIcon(IconRegistry.getScaled("GAME_LOST"));
			timer.pause();
	}
	
	//this is to reset the game board
	private void gameIconActionPerformed() {

	    gameIcon.setIcon(IconRegistry.getScaled("NEUTRAL"));
	    timer.reset();

	    // Step 1: Ask if user wants custom values
	    int customChoice = JOptionPane.showConfirmDialog(
	        gameIcon,
	        "Use custom board values?",
	        "Game Setup",
	        JOptionPane.YES_NO_OPTION
	    );

	    // YES → custom values
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
	            gameIcon,
	            inputs,
	            "Enter Custom Values",
	            JOptionPane.OK_CANCEL_OPTION
	        );

	        if (inputResult == JOptionPane.OK_OPTION) {
	            int rows = Integer.parseInt(rowField.getText());
	            int cols = Integer.parseInt(colField.getText());
	            int mines = Integer.parseInt(mineField.getText());
	            previousRows = rows;
	            previousCols = cols;
	            previousMines = mines;
	            manager = new GameManager(rows, cols, mines, this);
	        }

	        return;
	    }

	    // Step 2: Not custom → ask default vs previous
	    int reuseChoice = JOptionPane.showConfirmDialog(
	        gameIcon,
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
	
	public JPanel getGameBoard() {
		return this.boardPane;
	}
	
	public void setGameBoard(JPanel boardPane) {
		this.boardPane = boardPane;
		pack();
	}
	
	
}