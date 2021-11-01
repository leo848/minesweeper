package io.github.leo848;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.github.leo848.Constants.*;
import static java.awt.MouseInfo.*;

public class MinesweeperCanvas extends JPanel implements OptionalMouseListener {
	private static final Random random = new Random();
	final Vector mouse = new Vector();
	
	private final transient GameLoop gameLoop;
	
	List<List<Tile>> grid;
	private Graphics2D g2D;
	private boolean gameOver;
	
	public MinesweeperCanvas(GameLoop gameLoop) {
		this.gameLoop = gameLoop;
		setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		
		fillGrids();
		
		distributeMines();
		grid.forEach(tiles -> tiles.forEach(Tile::getNearbyMines));
	}
	
	public void distributeMines() {
		List<Integer> possibilities = IntStream.range(0, TILES)
		                                       .boxed()
		                                       .collect(Collectors.toList());
		
		for (int i = 0; i < NUMBER_OF_MINES; i++) {
			int randomNumber = possibilities.get(random.nextInt(possibilities.size()));
			grid.get(randomNumber / GRID_WIDTH)
			    .get(randomNumber % GRID_HEIGHT).isMine = true;
			possibilities.remove((Integer) randomNumber);
		}
	}
	
	public void fillGrids() {
		grid = new ArrayList<>(GRID_WIDTH);
		
		for (int x = 0; x < GRID_WIDTH; x++) {
			grid.add(new ArrayList<>(GRID_HEIGHT));
			for (int y = 0; y < GRID_HEIGHT; y++) {
				grid.get(x)
				    .add(new Tile(x, y, grid));
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		Tile tile = determineMouseTile();
		
		if (tile.isVisible) return;
		
		switch (e.getButton()) {
			case 1 -> {
				if (tile.isMine) gameOver();
				else {
					tile.makeVisible();
					tile.recursivelyUncoverNeighboringTiles();
				}
			}
			case 3 -> {
				if (tile.isMine) tile.makeVisible();
				else gameOver();
			}
			default -> {
				// other buttons
			}
		}
	}
	
	public void gameOver() {
		uncoverAllTiles();
		
		gameLoop.frame.repaint();
		gameOver = true;
	}
	
	/**
	 * @return nearest tile in grid based on current mouse position
	 */
	private Tile determineMouseTile() {
		Vector vector = mouse.copy();
		
		int x = (int) ((vector.x - (vector.x % SCALE_WIDTH)) / SCALE_WIDTH);
		int y = (int) ((vector.y - (vector.y % SCALE_HEIGHT)) / SCALE_HEIGHT);
		
		x = Math.min(Math.max(x, 0), GRID_WIDTH - 1);
		y = Math.min(Math.max(y, 0), GRID_HEIGHT - 1);
		
		return grid.get(x)
		           .get(y);
	}
	
	@Override
	public void paint(Graphics graphics) {
		g2D = (Graphics2D) graphics;
		initGraphics();
		
		mouse.set(new Vector(getPointerInfo().getLocation()).sub(new Vector(getLocationOnScreen())));
		
		if (gameOver) drawGameOver();
		
		drawGrid(g2D);
		
		determineMouseTile().showHighlighted(g2D);
	}
	
	private void drawGrid(Graphics2D g2D) {
		grid.stream()
		    .flatMap(Collection::stream)
		    .forEachOrdered(tile -> tile.show(g2D));
	}
	
	private void drawGameOver() {
		drawGrid(g2D);
		drawGameOverString(g2D);
	}
	
	private void drawGameOverString(Graphics2D g2D) {
		g2D.setColor(new Color(0x0));
		g2D.drawString("Game over!", 20, 20);
	}
	
	private void initGraphics() {
		g2D.setFont(DEFAULT_FONT);
		g2D.setColor(new Color(0xcccccc));
		g2D.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
	}
	
	private void uncoverAllTiles() {
		grid.stream()
		    .flatMap(Collection::stream)
		    .forEach(Tile::makeVisible);
	}
}
