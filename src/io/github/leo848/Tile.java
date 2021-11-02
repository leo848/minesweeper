package io.github.leo848;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static io.github.leo848.Constants.*;

public class Tile {
	final int x, y;
	private final List<List<Tile>> grid;
	Integer nearbyMines;
	private boolean isMine = false;
	private boolean isVisible = false;
	
	public Tile(int x, int y, List<List<Tile>> grid) {
		this.x = x;
		this.y = y;
		this.grid = grid;
	}
	
	public static boolean allVisible(List<List<Tile>> grid) {
		return grid.stream()
		           .flatMap(Collection::stream)
		           .filter(Objects::nonNull)
		           .allMatch(Tile::isVisible);
	}
	
	public boolean isVisible() {
		return isVisible;
	}
	
	public void setVisible(boolean visible) {
		isVisible = visible;
	}
	
	public static boolean noneVisible(List<List<Tile>> grid) {
		return grid.stream()
		           .flatMap(Collection::stream)
		           .filter(Objects::nonNull)
		           .noneMatch(Tile::isVisible);
	}
	
	private static boolean notAMine(Tile tile) {
		return !tile.isMine();
	}
	
	private static boolean notVisible(Tile tile) {
		return !tile.isVisible();
	}
	
	public boolean allMinesDefused() {
		return getNonDefusedMines() == getNearbyMines();
	}
	
	public void recursivelyUncoverNeighboringTiles() {
		getNeighbors().stream()
		              .filter(Tile::notVisible)
		              .filter(Tile::notAMine)
		              .filter(tile -> tile.getNeighbors()
		                                  .stream()
		                                  .anyMatch(Tile::allMinesDefused))
		              .peek(Tile::makeVisible)
		              .forEach(Tile::recursivelyUncoverNeighboringTiles);
	}
	
	@Override
	public int hashCode() {
		int result = x;
		result = 31 * result + y;
		result = 31 * result + (isVisible() ? 1 : 0);
		return result;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof final Tile tile)) return false;
		
		if (x != tile.x) return false;
		if (y != tile.y) return false;
		return isVisible() == tile.isVisible();
	}
	
	public void show(Graphics2D g2D) {
		if (!isVisible()) {
			showScaledRect(g2D, x, y, new Color(0xdadbdc));
			return;
		}
		
		int mineDisplay;
		if (UPDATE_NEARBY_MINES) {
			mineDisplay = (int) (getNearbyMines() - getNonDefusedMines());
		} else {
			mineDisplay = getNearbyMines();
		}
		
		if (isMine()) {
			showScaledRect(g2D, x, y, new Color(0xff0000));
		} else if (mineDisplay == 0) {
			showScaledRect(g2D, x, y, new Color(0xaaaaaa));
		} else {
			showScaledRect(g2D, x, y, new Color(0xafafaf));
			g2D.setColor(new Color(0x0));
			drawTextHere(g2D, Integer.toString(mineDisplay));
			
			if (allMinesDefused()) showScaledRect(g2D, x, y, new Color(0x1600ff00, true));
		}
	}
	
	public boolean isMine() {
		return isMine;
	}
	
	public void setMine(boolean mine) {
		isMine = mine;
	}
	
	public void makeVisible() {
		setVisible(true);
	}
	
	public int getNearbyMines() {
		if (nearbyMines == null) {
			nearbyMines = getNeighbors().stream()
			                            .filter(Tile::isMine)
			                            .toArray().length;
		}
		
		return nearbyMines;
	}
	
	private void drawTextHere(Graphics2D g2D, String text) {
		FontMetrics metrics = g2D.getFontMetrics(DEFAULT_FONT);
		
		g2D.setColor(Color.BLACK);
		g2D.drawString(text,
		               (int) (x * SCALE_WIDTH) + (int) (SCALE_WIDTH / 2) - (metrics.stringWidth(text) / 2),
		               (int) (y * SCALE_HEIGHT) + (int) (SCALE_HEIGHT / 2) -
		               ((metrics.getHeight() / 2) - metrics.getAscent()));
	}
	
	public void showHighlighted(Graphics2D g2D) {
		Color highlightColor = new Color(0x24_000000, true);
		showScaledRect(g2D, x, y, highlightColor);
		
		List<Tile> neighbors = getNeighbors();
		neighbors.forEach(tile -> showScaledRect(g2D, tile.x, tile.y, highlightColor));
		
		g2D.setStroke(new BasicStroke(3));
		g2D.drawRect((int) (neighbors.get(0).x * SCALE_WIDTH),
		             (int) (neighbors.get(0).y * SCALE_WIDTH),
		             (int) ((neighbors.get(neighbors.size() - 1).x - neighbors.get(0).x + 1) * SCALE_WIDTH),
		             (int) ((neighbors.get(neighbors.size() - 1).y - neighbors.get(0).y + 1) * SCALE_HEIGHT));
	}
	
	private void showScaledRect(Graphics2D g2D, int x, int y, Color color) {
		g2D.setColor(color);
		g2D.fillRect((int) (x * SCALE_WIDTH), (int) (y * SCALE_HEIGHT), (int) SCALE_WIDTH, (int) SCALE_HEIGHT);
		
		g2D.setColor(new Color(0x0));
		g2D.drawLine((int) (x * SCALE_WIDTH), 0, (int) (x * SCALE_WIDTH), GAME_HEIGHT);
		g2D.drawLine(0, (int) (y * SCALE_HEIGHT), GAME_WIDTH, (int) (y * SCALE_HEIGHT));
	}
	
	List<Tile> getNeighbors() {
		List<Tile> neighbors = new ArrayList<>();
		for (int tempX = -1; tempX <= 1; tempX++) {
			for (int tempY = -1; tempY <= 1; tempY++) {
				neighbors.add(safeArrayListAccess(x + tempX, y + tempY));
			}
		}
		return neighbors.stream()
		                .filter(Objects::nonNull)
		                .toList();
	}
	
	private Tile safeArrayListAccess(int x, int y) {
		try {
			return grid.get(x)
			           .get(y);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	private long getNonDefusedMines() {
		return getNeighbors().stream()
		                     .filter(Tile::isMine)
		                     .filter(Tile::isVisible)
		                     .count();
	}
	
	@Override
	public String toString() {
		return "Tile{" +
		       "isMine=" +
		       isMine() +
		       ", x=" +
		       x +
		       ", y=" +
		       y +
		       ", nearbyMines=" +
		       nearbyMines +
		       ", isVisible=" +
		       isVisible() +
		       '}';
	}
}
