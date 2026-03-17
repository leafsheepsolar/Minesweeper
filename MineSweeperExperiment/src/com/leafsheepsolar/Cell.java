package com.leafsheepsolar;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

public class Cell extends JButton {
	static private int cellSize = 22;
	
	final private boolean isMine;
	final private int row;
	final private int col;
	private boolean isRevealed;
	private int adjacentMines; // -1 if isMine is true
	private boolean isFlagged;
	protected GameManager manager;
//	protected boolean chordOn;
//this can be for later, depending if I want chord mode or not
	
	public Cell(int row, int col, boolean isMine, int adjacentMines, GameManager manager) {
		this.adjacentMines = adjacentMines;
		this.isMine = isMine;
		this.row = row;
		this.col = col;
		this.manager = manager;
		isRevealed = false; //all cells start blank
		isFlagged = false;
		configureButton();
		addActions();
//		chordOn = manager.isChordOn();
		
	}
	
	private void configureButton() {
		this.setBounds(new Rectangle(22,22));
	    setBorderPainted(false);
	    setFocusable(false);
	    setContentAreaFilled(false);
	    setIcon(IconRegistry.getScaled("UNREVEALED", "CELL"));
	}

	
	private boolean canChord() {
		if(isRevealed && !isMine && adjacentMines == manager.adjacentCellsFlagged(row,col)) {//not blank? -> cannot be a mine, unrevealed, or have a different number of adjacent cells flagged than adjacent mines
			return true;
		}
	
		return false;
	}
	
	private boolean canFloodFill() {
		if(isRevealed == false && isFlagged == false) {//must be unrevealed, unflagged, not a mine
			return true;
		}
		
		return false;
	}


	//adds actions for right & left clicks () 
	private void addActions() {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(SwingUtilities.isLeftMouseButton(e)) {// left click --> reveals tile, chording
					
					if(canFloodFill()) {
						floodFill();
					}
					if(canChord()) {
						chord();
					}
					
				} else if (SwingUtilities.isRightMouseButton(e)) {// right click --> places/removes flags
					if(!isRevealed) {
						flag();
					}
				}
			if(manager.getStartTimer()) {//if the timer is ready to be started, start
				manager.startTimerWorkaround();
			}
			;}
			
		});
	}

	protected void chord() {
		manager.chord(this);
	}
	
	/**
	 * initiates the revealing process of cells
	*/
	public void floodFill() {
		manager.floodFill(this);
	}

	//flagging the given tile
	public void flag() {
		
		//must be unrevealed to be flagged
		if(!isRevealed ) {
			if(!isFlagged) {//if it isnt already flagged, flag it
				setIcon(IconRegistry.getScaled("FLAG","CELL"));
				isFlagged = true;
				manager.addFlag();
			}
			else {//if it is already flagged, unflag it
				setIcon(IconRegistry.getScaled("UNREVEALED","CELL"));
				isFlagged = false;
				manager.removeFlag();
			}
		}
	}

	/**
	 * Reveals the cell
	 */	
	public void reveal() {
		if(!isRevealed) {
			isRevealed = true;
			if(!isMine) {
				//sets the icon based on the num of adjacent mines
				switch(adjacentMines) {
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
				manager.addRevealedCell();
				return;
			}
			if(manager.getGameLost() == false) {//if this mine was the cause of the loss, color it red
				setIcon(IconRegistry.getScaled("CLICKED_MINE","CELL"));
				manager.gameLost();
			}else {
				setIcon(IconRegistry.getScaled("REVEALED_MINE","CELL"));//if it was not the first mine revealed, then the game has already been lost and this isnt the cause. give this mine a blank background
			}
		}
	}
	
// getters and setters
	public boolean isRevealed() {
		return isRevealed;
	}
//getter
	public boolean isFlagged() {
		return isFlagged;
	}
//getter
	public boolean isMine() {
		return isMine;
	}
	
	// -1 if isMine == true;
	public int getAdjacentMines() {
		return adjacentMines;
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
		return "Column: " + col + ", row: " + row + ", is a mine: " + isMine + ", is revealed: " + isRevealed
				+ ", number of adjacent mines: " + adjacentMines + "." + ", current Icon: " + this.getIcon();
	}
}
