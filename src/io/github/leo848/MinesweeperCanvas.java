package io.github.leo848;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;
import java.util.stream.*;

import static java.awt.MouseInfo.*;
import static java.lang.Math.*;

public class MinesweeperCanvas extends JPanel implements MouseClickListener {
	final static private Random random = new Random();
	final Vector scale = new Vector();
	final Vector mouse = new Vector();
	Vector size = new Vector(500, 500);
	private final GameLoop gameLoop;
	List<List<Tile>> grid;
	private Graphics2D g2D;
	
	public MinesweeperCanvas(GameLoop gameLoop) {
		this.gameLoop = gameLoop;
		setPreferredSize(new Dimension((int) size.x, (int) size.y));
		
		int xTiles = 16;
		int yTiles = 16;
		
		fillGrids(xTiles, yTiles);
		scale.set(size.x / yTiles, size.y / yTiles);
		
		distributeMines(40);
		grid.forEach(tiles -> tiles.forEach(Tile::getNearbyMines));
	}
	
	public void distributeMines(int amount) {
		List<Integer> possibilities = IntStream.range(0, grid.size() * grid.get(0).size())
		                                       .boxed()
		                                       .collect(Collectors.toList());
		
		for (int i = 0; i < amount; i++) {
			int randomNumber = possibilities.get(random.nextInt(possibilities.size()));
			grid.get(randomNumber / grid.size()).get(randomNumber % grid.size()).isMine = true;
			possibilities.remove((Integer) randomNumber);
		}
	}
	
	public void fillGrids(int width, int height) {
		grid = new ArrayList<>(width);
		
		for (int x = 0; x < width; x++) {
			grid.add(new ArrayList<>(height));
			for (int y = 0; y < height; y++) {
				grid.get(x).add(new Tile(x, y, grid));
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		Vector index = determineMouseGrid();
		Tile tile = grid.get((int) index.x).get((int) index.y);
		
		if (tile.isVisible) return;
		
		switch (e.getButton()) {
			case 1 -> {
				if (tile.isMine) gameOver(g2D);
				else if (tile.nearbyMines != 0) uncoverTile(tile);
				else tile.recursivelyUncoverNeighboringTiles();
			}
			case 2 -> {
			}
			case 3 -> {
				if (tile.isMine) uncoverTile(tile);
				else gameOver(g2D);
			}
			default -> throw new IllegalStateException("Unexpected value: " + e.getButton());
		}
	}
	
	private Vector determineMouseGrid() {
		Vector vector = mouse.copy();
		
		vector.x -= vector.x % scale.x;
		vector.y -= vector.y % scale.y;
		
		vector.div(scale);
		return vector;
	}
	
	public void gameOver(Graphics2D g2D) {
		uncoverAllTiles();
		g2D.setColor(new Color(0xfffffff));
		g2D.drawString("Game over!", 20, 20);
		super.paint(g2D);
		paint(g2D);
		gameLoop.freeze();
		System.out.println("frozen");
	}
	
	private void uncoverAllTiles() {
		grid.forEach(tiles -> tiles.forEach(Tile::makeVisible));
	}
	
	@Override
	public void paint(Graphics graphics) {
		g2D = (Graphics2D) graphics;
		mouse.set(new Vector(getPointerInfo().getLocation()).sub(new Vector(getLocationOnScreen())));
		
		g2D.setColor(new Color(0x0));
		g2D.setFont(new Font("Arial", Font.PLAIN, 25));
		g2D.fillRect(0, 0, 500, 500);
		
		drawGrid(g2D);
		
		determineMouseGrid();
	}
	
	private void drawGrid(Graphics2D g2D) {
		for (int x = 0; x < grid.size(); x++) {
			for (int y = 0; y < grid.get(x).size(); y++) {
				if (!grid.get(x).get(y).isVisible) {
					g2D.setColor(new Color(0b1010_1011_1100_1101_1110_1111));
					scaledRect(g2D, x, y, 1);
					continue;
				}
				
				Tile tile = grid.get(x).get(y);
				
				int mines = tile.nearbyMines;
				if (tile.isMine) {
					g2D.setColor(new Color(0xff0000));
					scaledRect(g2D, x, y, .7);
				} else if (mines == 0) {
					g2D.setColor(new Color(0x232323));
					scaledRect(g2D, x, y, .9);
				} else {
					g2D.setColor(new Color(0x232323));
					scaledRect(g2D, x, y, .9);
					g2D.setColor(new Color(0x0));
					drawStringInGrid(g2D, Integer.toString(mines), x, y);
				}
			}
		}
	}
	
	private void uncoverTile(Tile tile) {
		tile.makeVisible();
	}
	
	private void scaledRect(Graphics2D g2D, int x, int y, double scaleFactor) {
		g2D.fillRect((int) ((x + 1 - scaleFactor) * scale.x),
		             (int) ((y + 1 - scaleFactor) * scale.y),
		             (int) (scale.x * pow(scaleFactor, 2)),
		             (int) (scale.y * pow(scaleFactor, 2)));
	}
	
	void drawStringInGrid(Graphics2D g2D, String text, int x, int y) {
		FontMetrics metrics = g2D.getFontMetrics(g2D.getFont());
		
		x = getMiddleScaledX(x);
		y = getMiddleScaledY(y);
		
		x -= metrics.stringWidth(text) / 2;
		y -= (metrics.getHeight() / 2 - metrics.getAscent());
		
		g2D.setColor(new Color(0xFFFFFF));
		g2D.drawString(text, x, y);
	}
	
	private int getMiddleScaledY(int y) {
		return (int) (y * scale.y + scale.y / 2);
	}
	
	private int getMiddleScaledX(int x) {
		return (int) (x * scale.x + scale.x / 2);
	}
}
