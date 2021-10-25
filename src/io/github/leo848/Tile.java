package io.github.leo848;

import java.util.*;

public class Tile {
	private final List<List<Tile>> grid;
	int x;
	int y;
	boolean visible = false;
	int nearbyMines;
	
	public Tile(int x, int y, List<List<Tile>> grid) {
		this.x = x;
		this.y = y;
		this.grid = grid;
	}
	
	public int getNearbyMines() {
		nearbyMines = getNeighbors();
		
		return nearbyMines;
	}
	
	private List<Tile> getNeighbors() {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
			
			}
		}
	}
}
