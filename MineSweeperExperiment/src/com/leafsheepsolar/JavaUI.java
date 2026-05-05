package com.leafsheepsolar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;

public class JavaUI extends JFrame {

	private JPanel contentPane, nPanel, cPanel;
	private JButton gameIndicator;
	private StopwatchLabel timer;
	private JLabel mineCounter;
	private GameManager manager;
	final int defaultRows = 9, defaultCols = 9, defaultMines = 10;
//	final int defaultRows = 16, defaultCols = 30, defaultMines = 99;
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//setting the frame  and contentpane
		setBounds(100, 100, 500, 600);
		setResizable(false);
		contentPane = new JPanel(new BorderLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		//set northpanel
		nPanel = new JPanel(null);
		nPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED,
				new Color(119,128,135),
				new Color(29,38,45),
				new Color(103,112,119),
				new Color(42,51,58)));
		nPanel.setPreferredSize(new Dimension(0,51));//width is automatically ignored
		contentPane.add(nPanel, BorderLayout.NORTH);
		
		//set minecounter
		mineCounter = new JLabel();
		mineCounter.setHorizontalAlignment(SwingConstants.TRAILING);
		Font configureFont;//set the custom font
	    try {
	    	configureFont = Font.createFont(Font.TRUETYPE_FONT,getClass().getResourceAsStream("/alarm clock.ttf"));
	    	configureFont = configureFont.deriveFont(Font.BOLD,28f);
	    }
	    catch (FontFormatException | IOException e){
	    	e.printStackTrace();
	    	configureFont = new Font("Tahoma", Font.PLAIN, 28);
	    }
	    mineCounter.setFont(configureFont);
		mineCounter.setBorder(new SoftBevelBorder(BevelBorder.LOWERED,
				new Color(119,128,135),
				new Color(29,38,45),
				new Color(103,112,119),
				new Color(42,51,58)));
		mineCounter.setBounds(10, 5, 75, 40);
		nPanel.add(mineCounter);
		
		//set timer
		timer = new StopwatchLabel();
		timer.setBounds(383, 5, 75, 40);
		nPanel.add(timer);
		//showing 000 on the timer
		timer.startTimer();
		timer.pause();
		
		//set gameIndicator
		gameIndicator = new JButton();
		gameIndicator.setIcon(IconRegistry.getScaled("NEUTRAL","GAME_INDICATOR"));
		gameIndicator.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				resetBoard(); //the dialogue is a popup
			}
		});
		gameIndicator.setBounds(getWidth()/2 - 20, nPanel.getY()+3, 40, 40);
		gameIndicator.setBorder(new SoftBevelBorder(BevelBorder.LOWERED,
				new Color(119,128,135),
				new Color(29,38,45),
				new Color(103,112,119),
				new Color(42,51,58)));
		nPanel.add(gameIndicator);

		//components in nPanel are resized proportionally
		addComponentListener(new ComponentAdapter() {
		    @Override
		    public void componentResized(ComponentEvent e) {
				gameIndicator.setBounds(getWidth()/2 - 30, 5, 40, 40);
				timer.setBounds(getWidth()-105, 5, 70, 40);
		    }
		});
		
		//give previousRows a default value
		previousRows = defaultRows;
		previousCols = defaultCols;
		previousMines = defaultMines;
		
		manager = new GameManager(defaultRows, defaultCols, defaultMines, this);
	}
	
	//reset board/enter settings for next board
	private void resetBoard() {

	    gameIndicator.setIcon(IconRegistry.getScaled("NEUTRAL","GAME_INDICATOR"));
	    timer.reset();

	    // ask if user wants custom values
	    int inputSettings = JOptionPane.showConfirmDialog(
	        gameIndicator,
	        "Use custom board values?",
	        "Game Setup",
	        JOptionPane.YES_NO_OPTION
	    );

	    // yes -> go to custom options
	    if (inputSettings == JOptionPane.YES_OPTION) {

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
	            "Enter values as integers",
	            JOptionPane.OK_CANCEL_OPTION
	        );

	        if (inputResult == JOptionPane.OK_OPTION) {
	        	int rows;
	        	int cols;
	        	int mines;
				try {
	        		rows = Integer.parseInt(rowField.getText().trim());
	        		cols = Integer.parseInt(colField.getText().trim());
	        		mines = Integer.parseInt(mineField.getText().trim());
	        	}catch(Exception e){
	        		JOptionPane.showMessageDialog(
	        				mineField,
	        				"Please enter integers",
	        				"Error: field(s) are null",
	        				JOptionPane.WARNING_MESSAGE);
	        		return;//TODO: change so the exit allows user to re-enter values (go to the previous JOptionPane object)
	        		
	        	}
	        	
	        	if(rows*cols < mines){
	        		JOptionPane.showMessageDialog(
		            		mineField, 
		            		"Mines need to be less than the total number of cells, please re-enter values",
		            		"Error: mines greater than number of cells",
		            		JOptionPane.WARNING_MESSAGE);
	        		return;
	        		//exit while allowing user to re-enter values
	        	}
	        	
	        	if((rows < defaultCols && cols < defaultRows) || (rows < defaultRows && cols < defaultCols) || cols < defaultCols){
	        		JOptionPane.showMessageDialog(
		            		rowField, 
		            		"The grid must be larger than "+defaultRows+", "+defaultCols+".",
		            		"Error: grid too small",
		            		JOptionPane.WARNING_MESSAGE);
	        		return;
	        		//exit while allowing user to re-enter values
	        	}
	        	
//	        	if(Cell.getCellSize() * cols < getWidth() || Cell.getCellSize() * rows < getHeight()) {
//					setMinimumSize(defualtMinSize);
//	        	}
	            manager = new GameManager(rows, cols, mines, this);
	            setPreviousSettings(rows,cols,mines);
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
		setMinimumSize(null);
	    pack();//TODO: investigate - its probably whats not working with the resizing componenets
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
		mineCounter.setText(text);
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