package com.leafsheepsolar;

public class BoardPresets {
	private int rows, cols, numMines;
	
	public BoardPresets(int rows, int cols, int numMines) {	
		this.rows = rows;
		this.cols = cols;
		this.numMines = numMines;
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public int getNumMines() {
		return numMines;
	}
}
