package com.leafsheepsolar;

import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

public class GameManager {

    private final int rows;
    private final int cols;
    private Cell[][] grid;
    private int[][] intGrid;//simplifying the randomizing
    private final int mines; 
    private int numFlags;
    private boolean startTimer;
    private boolean gameLost; 
    private int numCorrectFlags;
    private int numRevealedCells;
    private JavaUI parent;

    
	/** <p> Manages making the cell board and the methods associated with it. */
    public GameManager(int rows, int cols, int mines, JavaUI parent) {
    	this.parent = parent;
        this.mines = mines;
        this.rows = rows;
        this.cols = cols;
        gameLost = false;
        startTimer = true;
        grid = new Cell[rows][cols];
        intGrid = new int[rows][cols];
        initialize();
    }
    
    private void initialize() {
    	placeMines();
    	
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if(intGrid[r][c]==1) {
                	//construct cell grid from the int grid
                    grid[r][c] = new Cell(r, c, true, -1, this);
                }
                else {
                    grid[r][c] = new Cell(r, c, false, countAdjacentMines(r,c), this);
                }
            }
        }
        
        createBoard();
        parent.setPreviousSettings(rows,cols,mines);
    }
    
    /* place mines in intGrid
     * 1 = mine, 0 = not mine. Given mines, randomize mine placement within the int[][] intGrid
     */
    private void placeMines() {
        //starts the array with only 0's
    	for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
            	intGrid[r][c] = 0;
            }
    	}
           
    	//place the mines in the filled array
    	int placed = 0;
        Random rand = new Random();
        
        while (placed <= mines) {
            int r = rand.nextInt(rows);
            int c = rand.nextInt(cols);

	        if (intGrid[r][c] == 0) {     // Prevent duplicates
	            intGrid[r][c] = 1;
	            placed++;
	        }
        }
    }
    
    public void createBoard() {
	    int buttonSize = Cell.getCellSize();
	    
	    JPanel cPanel = new JPanel(new GridLayout(rows, cols));
	    cPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
	    int cPanelWidth = buttonSize * cols;
	    int cPanelHeight = buttonSize * rows;
	    Dimension cPanelDim = new Dimension(cPanelWidth, cPanelHeight);
	    cPanel.setPreferredSize(cPanelDim);

	    JLabel mineCounter = parent.getMineCounter();
	    
	    
	    
	    for (int r = 0; r < rows; r++)
	        for (int c = 0; c < cols; c++)
	            cPanel.add(grid[r][c]);
	    
	    parent.addBoard(cPanel);
	}
    
    // Count the number of adjacent mines for the Cell at the specified location.
    // Uses intGrid where 1 = mine, 0 = no mine.
    // Checks all 8 adjacent cells plus the cell itself with bounds checking.
    private int countAdjacentMines(int row, int col) {
        int mineCount = 0;

        // Check all 8 adjacent cells plus the center cell
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                // Bounds check 
                if (r >= 0 && r < rows && c >= 0 && c < cols) {
                    if (intGrid[r][c] == 1) {
                        mineCount++;
                    }
                }
            }
        }

        return mineCount;
    }

    //TODO: does it work?
    // Create all Cell objects once, if the cell is a bomb, adjacentMines = -1.
    
    public int adjacentCellsFlagged(int row, int col) {
        int flagCount = 0;

        // Check all 8 adjacent cells plus the center cell
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                // Bounds check 
                if (r >= 0 && r < rows && c >= 0 && c < cols) {
                    if (grid[r][c].isFlagged()) {
                        flagCount++;
                    }
                }
            }
        }

        return flagCount;
    }
    
	/**
	 * floodfills the given cell (assumes unrevealed)
	*/
    public void floodFill(Cell cell) {
    	if(!(cell.isRevealed())) {
    		System.out.println("This cell is already revealed");
    		return;
    	}
        int row = cell.getRow();
        int col = cell.getCol();
        cell.reveal();
        
       if(cell.getAdjacentMines() == 0 ) {
    	   
    	   for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                // Bounds check 
                if (r >= 0 && r < rows && c >= 0 && c < cols && !(grid[r][c].isRevealed())/*dont reveal a cell thats already revealed*/) {
                	floodFill(grid[r][c]);
                }
            }
        }
    	   
       }
    }
    
	/**
	 * chords the given cell
	*/
    public void chord(Cell cell) {
        int row = cell.getRow();
        int col = cell.getCol();

        //stop condition
        if (cell.isMine()) {
            return;
        }

        cell.reveal();

        //secondary stop condition, prevent numbered cells from expanding
        if (cell.getAdjacentMines() != 0) {
            return;
        }
        
        //reveal adjacent cells
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                // Bounds check 
                if (r >= 0 && r < rows && c >= 0 && c < cols) {
                	chord(grid[r][c]);
                }
            }
        }
    }

    //reveal incorrectly flagged mines
    public void gameLost() {
    	gameLost = true;//game has been lost, so set to true
    	parent.gameLost();
    	
    	for(int r = 0; r<rows; r++) {
    		for(int c = 0; c<cols; c++) {
    			if(grid[r][c].isMine() && !(grid[r][c].isFlagged())) { 
    				grid[r][c].reveal();//if a mine is not flagged then reveal the tile. If it is flagged and is a mine dont reveal
    				grid[r][c].setEnabled(false);
    			}
    		}
    	}
	}
    
    //get the mine at the specified location in the grid
    public int getIntInGrid(int r, int c) {
        return intGrid[r][c];
    }
    
    // Get a specific cell
    public Cell getCell(int r, int c) {
        return grid[r][c];
    }
    
    public int getPlacedFlags() {
    	return numFlags;
    }
    
	/** increases the flagcounter by one, checks if the game is won */
    public void addFlag(boolean isMine) {
    	if(isMine) {
    		numCorrectFlags++;
    		if(numCorrectFlags + numRevealedCells - 1 == rows*cols) {
				/*
				 * Locate the last unrevealed mine cell and flag it.
				 */
    		}
    		if(numCorrectFlags == mines - 1) { //if theres only one mine left to flag, youre done
    			parent.gameWon(); //TODO: fix this
    		}
    	}
    	numFlags++;
    	JLabel temp = parent.getMineCounter();
    	temp.setText(String.valueOf(numFlags));
    	parent.setMineCounter(temp);
    }
    
    // Lowers the count of flags
    public void removeFlag(boolean isMine) {
    	if(isMine) 
    		numCorrectFlags--;
    	numFlags--;
    	JLabel temp = parent.getMineCounter();
    	temp.setText(String.valueOf(numFlags));
    	parent.setMineCounter(temp);
    }
	
	/** adds 1 to the number of revealed cells */
    public void addRevealedCell() {
    	numRevealedCells++;
    }
    
    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getMines() {
        return mines;
    }

	public void setGameLost(boolean gameLost) {
		this.gameLost = gameLost;
	}
	
	public boolean getGameLost() {
		return gameLost;
	}

	public boolean getStartTimer() {
		return startTimer;
	}

	public void setStartTimer(boolean startTimer) {
		this.startTimer = startTimer;
	}
	
	public void startTimerWorkaround() {
		parent.startTimer();
	}

//WIP
//	public boolean isChordOn() {
//		return true;
//	}
}
