package com.leafsheepsolar;

import java.util.HashMap;
import java.util.Map;

public class BoardPresets {
	private static Map<String, GameManager> presets = new HashMap<>();
	
	
	
	public BoardPresets(int rows, int cols, int numMines, JavaUI Window) {
		new GameManager(rows, cols, numMines, Window);
	}
	
	
	
}
