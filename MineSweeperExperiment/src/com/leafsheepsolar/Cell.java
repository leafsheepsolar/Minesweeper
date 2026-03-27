package com.leafsheepsolar;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

public class Cell extends JButton {
	static protected int cellSize = 24; //in pizels
	
	final private int row;
	final private int col;
	final private boolean mine;
	private boolean flagged;
	private boolean revealed;
	private int adjMines; // -1 if mine is true
	protected GameManager manager;
//	protected boolean chordOn;
//this can be for later, depending if I want chord mode or not
	
	public Cell(int row, int col, boolean mine, int adjMines, GameManager manager) {
		this.adjMines = adjMines;
		this.mine = mine;
		this.row = row;
		this.col = col;
		this.manager = manager;
		revealed = false; //all cells start blank
		flagged = false;
		configureButton();
		addActions();
//		chordOn = manager.isChordOn();
		
	}
	
	protected void configureButton() {
		this.setBounds(new Rectangle(24,24));
	    setBorderPainted(false);
	    setFocusable(false);
	    setContentAreaFilled(false);
	    setIcon(IconRegistry.getScaled("UNREVEALED", "CELL"));
	}
	
	
	//must be blank, unflagged, and same num of adjMines as adjFlags
	protected boolean canChord() {
		if(revealed && !mine && adjMines == manager.adjacentCellsFlagged(row,col)) {
			return true;
		}
	
		return false;
	}
	
	public boolean canReveal() {
		if(!revealed && !flagged) {//must be unrevealed, unflagged
			return true;
		}
		
		return false;
	}

	//adds actions for right & left clicks () 
	protected void addActions() {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(SwingUtilities.isLeftMouseButton(e)) {// left click --> reveals cell, chording
					chord();
					floodReveal();

				} else if (SwingUtilities.isRightMouseButton(e)) {// right click --> places/removes flags
					flag();
				}
			if(manager.getStartTimer()) {//if the timer is ready to be started, start
				manager.startTimerWorkaround();
			}
			}
		});
	}

	protected void chord() {
		if(canChord()) {
			manager.chord(this);
		}
	}
	
	public void floodFill() {
		manager.floodFill(row,col);
	}
	
	/**
	 * plainly reveals the cell
	 * @see #floodReveal
	 */	
	public void reveal() {
		if(canReveal()) {
			setCellIcon();
			revealed = true;
			manager.addRevealedCell();
		}
	}
	
	/**
	 * reveals, floodfills if it can
	 * 
	 * @see #reveal()
	 */
	public void floodReveal() {
		if(canReveal()) {
			if(adjMines == 0) {
				floodFill();
				return;
			}
			reveal();
		}
	}
	
	protected void setCellIcon() {
		if(!mine) {
			//sets the icon based on the num of adjacent mines
			switch(adjMines) {
			case 0 -> setIcon(IconRegistry.getScaled("EMPTY","CELL"));
			case 1 -> setIcon(IconRegistry.getScaled("ONE","CELL"));
	        case 2 -> setIcon(IconRegistry.getScaled("TWO","CELL"));
	        case 3 -> setIcon(IconRegistry.getScaled("THREE","CELL"));
	        case 4 -> setIcon(IconRegistry.getScaled("FOUR","CELL"));
	        case 5 -> setIcon(IconRegistry.getScaled("FIVE","CELL"));
	        case 6 -> setIcon(IconRegistry.getScaled("SIX","CELL"));
	        case 7 -> setIcon(IconRegistry.getScaled("SEVEN","CELL"));
	        case 8 -> setIcon(IconRegistry.getScaled("EIGHT","CELL"));
	        default -> {
	        	System.out.println("Switching on adjMines returned default for cell at row "+row+" and col "+col+".");
	        	setIcon(IconRegistry.getScaled("EIGHT","CELL"));		
	        	}
			}
			return;
		}
		if(manager.getGameLost() == false) {//if this mine was the cause of the loss, primary color will be red
			setIcon(IconRegistry.getScaled("CLICKED_MINE","CELL"));
			manager.gameLost();
		}else {
			setIcon(IconRegistry.getScaled("REVEALED_MINE","CELL"));//otherwise regular background
		}
	}
	
	//flag the given cell
	public void flag() {
		if(!revealed) {
			if(!flagged) {//if it isnt already flagged, flag it
				setIcon(IconRegistry.getScaled("FLAG","CELL"));
				flagged = true;
				manager.addFlag();
			}
			else {//if it is already flagged, unflag it
				setIcon(IconRegistry.getScaled("UNREVEALED","CELL"));
				flagged = false;
				manager.removeFlag();
			}
		}
	}

	public boolean isRevealed() {
		return revealed;
	}

	public boolean isFlagged() {
		return flagged;
	}

	public boolean isMine() {
		return mine;
	}
	
	// -1 if mine == true;
	public int getAdjacentMines() {
		return adjMines;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	/**
	 * <p>Returns the size in pixels of a cell.
	 * 
	 * <p> Only returns one value because cells are square.
	 */
	public static int getCellSize() {
		return cellSize;
	}
	
	@Override
	public String toString() {
		return "Column: " + col + ", row: " + row + ", is a mine: " + mine + ", is revealed: " + revealed + 
				", is flagged: " + flagged + ", number of adjacent mines: " + adjMines + "." + "Current Icon: " + this.getIcon();
	}
}
