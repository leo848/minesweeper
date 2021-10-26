package io.github.leo848;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;
import java.util.stream.*;

import static io.github.leo848.Constants.*;
import static java.awt.MouseInfo.*;

public class MinesweeperCanvas extends JPanel implements MouseClickListener {
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
		Vector index = determineMouseGrid();
		Tile tile = grid.get((int) index.x)
		                .get((int) index.y);
		
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
	
	private void uncoverAllTiles() {
		grid.forEach(tiles -> tiles.forEach(Tile::makeVisible));
	}
	
	private Vector determineMouseGrid() {
		Vector vector = mouse.copy();
		
		vector.x -= vector.x % SCALE_WIDTH;
		vector.y -= vector.y % SCALE_HEIGHT;
		
		vector.div(SCALE_VECTOR);
		return vector;
	}
	
	@Override
	public void paint(Graphics graphics) {
		g2D = (Graphics2D) graphics;
		initGraphics();
		
		mouse.set(new Vector(getPointerInfo().getLocation()).sub(new Vector(getLocationOnScreen())));
		
		if (gameOver) drawGameOver();
		
		drawGrid(g2D);
	}
	
	private void drawGameOver() {
		drawGrid(g2D);
		drawGameOverString(g2D);
	}
	
	private void drawGameOverString(Graphics2D g2D) {
		g2D.setColor(new Color(0x0));
		g2D.drawString("Game over!", 20, 20);
	}
	
	private void drawGrid(Graphics2D g2D) {
		for (List<Tile> tiles: grid) {
			for (Tile tile: tiles) {
				tile.show(g2D);
			}
		}
	}
	
	private void initGraphics() {
		g2D.setFont(DEFAULT_FONT);
		g2D.setColor(new Color(0xffffff));
		g2D.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
	}
}
