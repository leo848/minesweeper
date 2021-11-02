package io.github.leo848;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.github.leo848.Constants.*;
import static java.awt.MouseInfo.*;

public class MinesweeperCanvas extends JPanel implements OptionalMouseListener {
	private static final Random random = new Random();
	final Vector mouse = new Vector();
	private final transient GameLoop gameLoop;
	Display display = new Display(MSG_TUTORIAL);
	List<List<Tile>> grid;
	private Graphics2D g2D;
	private boolean gameOver;
	
	long startTime = System.nanoTime() / 1_000_000;
	long endTime;
	private boolean finished;
	
	public MinesweeperCanvas(GameLoop gameLoop) {
		this.gameLoop = gameLoop;
		setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		
		fillGrids();
		
		distributeMines();
		grid.stream()
		    .flatMap(Collection::stream)
		    .forEach(Tile::getNearbyMines);
	}
	
	public void distributeMines() {
		List<Integer> possibilities = IntStream.range(0, TILES)
		                                       .boxed()
		                                       .collect(Collectors.toList());
		
		for (int i = 0; i < NUMBER_OF_MINES; i++) {
			int randomNumber = possibilities.get(random.nextInt(possibilities.size()));
			grid.get(randomNumber / GRID_WIDTH)
			    .get(randomNumber % GRID_HEIGHT)
			    .setMine(true);
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
	public void mousePressed(MouseEvent e) {
		if (Objects.equals(display.getText(), MSG_TUTORIAL)) display.setText(MSG_AFTER_TUTORIAL);
		
		Tile tile = determineMouseTile();
		if (tile.isVisible()) return;
		
		switch (e.getButton()) {
			case 1 -> {
				if (tile.isMine()) gameOver(false);
				else {
					tile.makeVisible();
					tile.recursivelyUncoverNeighboringTiles();
				}
			}
			case 3 -> {
				if (tile.isMine()) tile.makeVisible();
				else gameOver(false);
			}
			default -> {
			}
		}
		
		if (Tile.allVisible(grid) && !gameOver) {
			gameOver(true);
		}
	}
	
	public void gameOver(boolean result) {
		endTime = System.nanoTime() / 1_000_000;
		
		if (result) {
			finished = true;
		}
		
		uncoverAllTiles();
		
		gameLoop.frame.repaint();
		gameOver = true;
	}
	
	@Override
	public void paint(Graphics graphics) {
		g2D = (Graphics2D) graphics;
		initGraphics();
		
		mouse.set(new Vector(getPointerInfo().getLocation()).sub(new Vector(getLocationOnScreen())));
		
		drawGrid(g2D);
		if (gameOver) {
			if (finished) {
				display.setText(String.format(MSG_SOLVED, formattedSubtractedTime(startTime, endTime)));
			} else display.setText(MSG_GAME_OVER);
		}
		
		display.show(g2D);
		
		determineMouseTile().showHighlighted(g2D);
	}
	
	private String formattedSubtractedTime(long startTime, long endTime) {
		long raw = endTime - startTime;
		int millis = (int) (raw % 1000);
		int minutes = (int) (raw / 60_000);
		int seconds = (int) raw / 1000;
		
		return "%d:%d.%d".formatted(minutes % 60, seconds % 60, millis % 1000);
	}
	
	private void drawGrid(Graphics2D g2D) {
		grid.stream()
		    .flatMap(Collection::stream)
		    .forEachOrdered(tile -> tile.show(g2D));
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
