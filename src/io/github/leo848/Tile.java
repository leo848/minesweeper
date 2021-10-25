package io.github.leo848;

import java.util.*;
import java.util.stream.*;

public class Tile {
	private final List<List<Tile>> grid;
	private final Random random = new Random();
	public boolean isMine = false;
	int x;
	int y;
	int nearbyMines;
	boolean isVisible = false;
	
	public Tile(int x, int y, List<List<Tile>> grid) {
		this.x = x;
		this.y = y;
		this.grid = grid;
	}
	
	private static boolean notAMine(Tile tile) {
		return !tile.isMine;
	}
	
	private static boolean notVisible(Tile tile) {
		return !tile.isVisible;
	}
	
	public void getNearbyMines() {
		nearbyMines = getNeighbors().stream().filter(tile -> tile.isMine).toArray().length;
	}
	
	private List<Tile> getNeighbors() {
		List<Tile> neighbors = new ArrayList<>();
		for (int tempX = -1; tempX <= 1; tempX++) {
			for (int tempY = -1; tempY <= 1; tempY++) {
				neighbors.add(safeArrayListAccess(x + tempX, y + tempY));
			}
		}
		return neighbors.stream().filter(Objects::nonNull).collect(Collectors.toList());
	}
	
	private Tile safeArrayListAccess(int x, int y) {
		try {
			return grid.get(x).get(y);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public void makeVisible() {
		isVisible = true;
	}
	
	public void recursivelyUncoverNeighboringTiles() {
		getNeighbors().stream()
		              .filter(Tile::notVisible)
		              .filter(Tile::notAMine)
		              .filter(tile -> tile.getNeighbors().stream().anyMatch(neighbor -> neighbor.nearbyMines == 0))
		              .peek(Tile::makeVisible)
		              .forEach(Tile::recursivelyUncoverNeighboringTiles);
	}
}
