package io.github.leo848;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.*;

import static io.github.leo848.Constants.*;

public class Tile {
	private final List<List<Tile>> grid;
	public boolean isMine = false;
	int x;
	int y;
	Integer nearbyMines;
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
	
	public void makeVisible() {
		isVisible = true;
	}
	
	public void recursivelyUncoverNeighboringTiles() {
		getNeighbors().stream()
		              .filter(Tile::notVisible)
		              .filter(Tile::notAMine)
		              .filter(tile -> tile.getNeighbors()
		                                  .stream()
		                                  .anyMatch(neighbor -> neighbor.nearbyMines == 0))
		              .peek(Tile::makeVisible)
		              .forEach(Tile::recursivelyUncoverNeighboringTiles);
	}
	
	public void show(Graphics2D g2D) {
		if (!isVisible) {
			showScaledRect(g2D, x, y, 0xaaaaaa);
			return;
		}
		
		if (isMine) {
			showScaledRect(g2D, x, y, 0xff0000);
		} else if (nearbyMines == 0) {
			showScaledRect(g2D, x, y, 0xcacaca);
		} else {
			showScaledRect(g2D, x, y, 0xacacac);
			g2D.setColor(new Color(0x0));
			drawTextHere(g2D, Integer.toString(getNearbyMines()));
		}
	}
	
	public int getNearbyMines() {
		if (nearbyMines == null) {
			nearbyMines = getNeighbors().stream()
			                            .filter(tile -> tile.isMine)
			                            .toArray().length;
		}
		
		return nearbyMines;
	}
	
	public List<Tile> getNeighbors() {
		List<Tile> neighbors = new ArrayList<>();
		for (int tempX = -1; tempX <= 1; tempX++) {
			for (int tempY = -1; tempY <= 1; tempY++) {
				neighbors.add(safeArrayListAccess(x + tempX, y + tempY));
			}
		}
		return neighbors.stream()
		                .filter(Objects::nonNull)
		                .collect(Collectors.toList());
	}
	
	private Tile safeArrayListAccess(int x, int y) {
		try {
			return grid.get(x)
			           .get(y);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	private void showScaledRect(Graphics2D g2D, int x, int y, int color) {
		g2D.setColor(new Color(color));
		g2D.fillRect(x * SCALE_WIDTH, y * SCALE_HEIGHT, SCALE_WIDTH, SCALE_HEIGHT);
	}
	
	private void drawTextHere(Graphics2D g2D, String text) {
		FontMetrics metrics = g2D.getFontMetrics(DEFAULT_FONT);
		
		int posX = (x * SCALE_WIDTH) + (SCALE_WIDTH / 2);
		int posY = (y * SCALE_HEIGHT) + (SCALE_HEIGHT / 2);
		
		posX -= metrics.stringWidth(text) / 2;
		posY -= (metrics.getHeight() / 2 - metrics.getAscent());
		
		g2D.setColor(Color.BLACK);
		g2D.drawString(text, posX, posY);
	}
}
