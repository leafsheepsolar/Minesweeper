package com.leafsheepsolar;

import java.util.Random;

import javax.swing.JPanel;

import java.awt.GridBagConstraints;

public class GameManager {

    private final int rows;
    private final int cols;
    private Cell[][] grid;
    private int[][] intGrid;//simplifying the randomizing
    private final int numMines;
    private int placedFlags;
    private boolean startTimer;
    private boolean gameLost; //game == 0 -> game isn't lost, game == anything else -> lost
    private int numCorrectFlags;
    private JavaUI parent;
    private JPanel boardPanel;
    private final static GridBagConstraints gbc = new GridBagConstraints();
    /* gameLost -> 1 means the game is lost and thus reveal only the first mine with the red background to indicate that it was the one chosen,
    *  whereas the rest of the mines will be revealed with normal backgrounds
    */
    public GameManager(int rows, int cols, int numMines, JavaUI parent) {
    	this.parent = parent;
        this.numMines = numMines;
        this.rows = rows;
        this.cols = cols;
        gameLost = false;
        startTimer = true;
        boardPanel = parent.getGameBoard();
        grid = new Cell[rows][cols];
        initialize();
    }
    
    private void initialize() {
    	placeMines();
    	
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if(intGrid[rows][cols]==1) {
                	//construct cell
                    grid[r][c] = new Cell(r, c, true, -1, this);
                }
                else {
                    grid[r][c] = new Cell(r, c, false, countAdjacentMines(r,c), this);
                }
            }
        }
        boardSetup();
    }
    
    /* place mines in intGrid
     * 1 = mine, 0 = not mine. Given numMines, randomize mine placement within the int[][] intGrid
     * TODO: does it work?
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
        
        while (placed <= numMines) {
            int r = rand.nextInt(rows);
            int c = rand.nextInt(cols);

	        if (intGrid[r][c] == 0) {     // Prevent duplicates
	            intGrid[r][c] = 1;
	            placed++;
	        }
        }
    }
    
    private void boardSetup() {//add the finished array to the JPanel
    	for(int r = 0; r<rows; r++) {
    		for(int c = 0; c<cols; c++) {
    			boardPanel.add(grid[r][c]);// use gbc when adding to the board. 
    		}
    	}
    }
    

    // Count the number of adjacent mines for the Cell at the specified location.
    // Uses intGrid where 1 = mine, 0 = no mine.
    // Checks all 8 adjacent cells plus the cell itself with bounds checking.
    private int countAdjacentMines(int row, int col) {
        int mineCounter = 0;

        // Check all 8 adjacent cells plus the center cell
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                // Bounds check to prevent out of bounds access
                if (r >= 0 && r < rows && c >= 0 && c < cols) {
                    if (intGrid[r][c] == 1) {
                        mineCounter++;
                    }
                }
            }
        }

        return mineCounter;
    }

    //TODO: does it work?
    // Create all Cell objects once, if the cell is a bomb, adjacentMines = -1.
    
    
    public int adjacentCellsFlagged(Cell cell) {
		int num = 0;
		int row = cell.getRow();
		int col = cell.getCol();
		
		for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                // Bounds check to prevent out of bounds access
                if (r >= 0 && r < rows && c >= 0 && c < cols) {
                    if (grid[r][c].isFlagged()){
                        num++;
                    }
                }
            }
        }
		
		return num;
	}

    public void chord(Cell cell) {
        int r = cell.getRow();
        int c = cell.getCol();

        // Bounds check
        if (r < 0 || r >= rows || c < 0 || c >= cols) {
            return;
        }

        // Stop conditions
        if (cell.isRevealed() || cell.isMine()) {
            return;
        }

        // Reveal current cell
        cell.reveal();

        // Stop expanding if numbered cell
        if (cell.getAdjacentMines() > 0) {
            return;
        }
        
        
        //int meanings -> dr, dc (delta row, delta column) delta meaning changes. nr, nc (new row, new column)
        
        // Recursively reveal neighbors
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr != 0 || dc != 0) {
                    int nr = r + dr;
                    int nc = c + dc;

                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                        chord(grid[nr][nc]);
                    }
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
    			}
    		}
    	}
    	for(int r = 0; r<rows; r++) {
    		for(int c = 0; c<cols; c++) {
    			grid[r][c].setEnabled(false);
    		}
    	}
	}
    
    public void isGameWon() {
    	if((numCorrectFlags + 1) == numMines) {
    		parent.gameWon();
    	}
    }
    //TODO check bounds of the cell grid?
    

    //get the mine at the specified location in the grid
    public int getIntInGrid(int r, int c) {
        return intGrid[r][c];
    }
    
    // Get a specific cell
    public Cell getCell(int r, int c) {
        return grid[r][c];
    }
    
    public int getPlacedFlags() {
    	return placedFlags;
    }
    //for increasing flag counter
    public void addFlag() {
    	placedFlags++;
    	if(placedFlags < numMines) {
    		parent.gameWon();//TODO fix this
    	}
    }
    
    public void removeFlag() {
    	placedFlags--;
    }
	
    // Rows count
    public int getRows() {
        return rows;
    }

    // Columns count
    public int getCols() {
        return cols;
    }

    // Mines count
    public int getMines() {
        return numMines;
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

//	public boolean isChordOn() {
//		return true;
//	}
//	
}
