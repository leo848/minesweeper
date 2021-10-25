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
	List<List<Integer>> grid;
	List<List<Boolean>> visible;
	
	public MinesweeperCanvas() {
		setPreferredSize(new Dimension((int) size.x, (int) size.y));
		
		int xTiles = 20;
		int yTiles = 20;
		
		fillGrids(xTiles, yTiles);
		scale.set(size.x / yTiles, size.y / yTiles);
		
		distributeMines(xTiles * yTiles / 20);
		calculateNeighbors();
	}
	
	private void calculateNeighbors() {
		for (int x = 0; x < grid.size(); x++) {
			for (int y = 0; y < grid.get(x).size(); y++) {
				if (grid.get(x).get(y) == -1) continue;
				
				grid.get(x).set(y, getNeighbors(new Vector(x, y)).size());
			}
		}
	}
	
	public List<Vector> getNeighbors(Vector tile) {
		List<Vector> neighbors = new ArrayList<>();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				
				neighbors.add(new Vector(i, j));
			}
		}
		return neighbors;
	}
	
	public void distributeMines(int amount) {
		List<Integer> possibilities = IntStream.range(0, grid.size() * grid.get(0).size())
		                                       .boxed()
		                                       .collect(Collectors.toList());
		
		for (int i = 0; i < amount; i++) {
			int randomNumber = possibilities.get(random.nextInt(possibilities.size()));
			grid.get(randomNumber / grid.size()).set(randomNumber % grid.size(), -1);
			possibilities.remove((Integer) randomNumber);
		}
	}
	
	public void fillGrids(int width, int height) {
		grid = new ArrayList<>(width);
		visible = new ArrayList<>(width);
		
		for (int x = 0; x < width; x++) {
			grid.add(new ArrayList<>(height));
			visible.add(new ArrayList<>(height));
			for (int y = 0; y < height; y++) {
				grid.get(x).add(0);
				visible.get(x).add(false);
			}
		}
	}
	
	private int safeArrayListAccess(int x, int y) {
		try {
			return grid.get(x).get(y);
		} catch (IndexOutOfBoundsException e) {
			return -2;
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		Vector index = determineMouseGrid();
		if (grid.get((int) index.x).get((int) index.y) == -1) uncoverAllTiles();
		visible.get((int) index.x).set((int) index.y, true);
	}
	
	/**
	 * Resets the visible ArrayList<List<Boolean>>.
	 */
	private void uncoverAllTiles() {
		visible.forEach(booleans -> IntStream.range(0, booleans.size()).forEach(j -> booleans.set(j, true)));
	}
	
	private Vector determineMouseGrid() {
		Vector vector = mouse.copy();
		
		vector.x -= vector.x % scale.x;
		vector.y -= vector.y % scale.y;
		
		vector.div(scale);
		return vector;
	}
	
	@Override
	public void paint(Graphics graphics) {
		Graphics2D g2D = (Graphics2D) graphics;
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
				if (!visible.get(x).get(y)) {
					g2D.setColor(new Color(0b1010_1011_1100_1101_1110_1111));
					scaledRect(g2D, x, y, 1);
					continue;
				}
				
				int number = grid.get(x).get(y);
				switch (number) {
					case 0 -> {
						g2D.setColor(new Color(0x232323));
						scaledRect(g2D, x, y, .9);
					}
					case -1 -> {
						g2D.setColor(new Color(0xff0000));
						scaledRect(g2D, x, y, .7);
					}
					default -> {
						g2D.setColor(new Color(0x232323));
						scaledRect(g2D, x, y, .9);
						g2D.setColor(new Color(0x0));
						drawStringInGrid(g2D, Integer.toString(number), x, y);
					}
				}
			}
		}
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
