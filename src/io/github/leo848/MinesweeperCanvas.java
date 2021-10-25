package io.github.leo848;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.*;

public class MinesweeperCanvas extends JPanel {
	final Vector scale = new Vector();
	private final Random random = new Random();
	Vector size = new Vector(500, 500);
	List<List<Integer>> grid;
	
	public MinesweeperCanvas(@SuppressWarnings("unused") GameLoop gameLoop) {
		
		setPreferredSize(new Dimension((int) size.x, (int) size.y));
		scale.set(size.x / 10, size.y / 10);
		
		fillGrid(10, 10);
		distributeMines(15);
		calculateNeighbors();
	}
	
	private void calculateNeighbors() {
		for (int x = 0; x < grid.size(); x++) {
			for (int y = 0; y < grid.get(x).size(); y++) {
				if (grid.get(x).get(y) == -1) continue;
				
				int i = 0; // neighbor count, this has to be done better in some random way
				i += safeArrayListCheck(x - 1, y - 1);
				i += safeArrayListCheck(x, y - 1);
				i += safeArrayListCheck(x + 1, y - 1);
				
				i += safeArrayListCheck(x - 1, y);
				i += safeArrayListCheck(x + 1, y);
				
				i += safeArrayListCheck(x - 1, y + 1);
				i += safeArrayListCheck(x, y + 1);
				i += safeArrayListCheck(x + 1, y + 1);
				
				grid.get(x).set(y, i);
			}
		}
	}
	
	private int safeArrayListCheck(int x, int y) {
		try {
			return grid.get(x).get(y).equals(-1) ? 1 : 0;
		} catch (IndexOutOfBoundsException e) {
			return 0;
		}
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
	
	public void fillGrid(int width, int height) {
		grid = new ArrayList<>(width);
		
		for (int x = 0; x < width; x++) {
			grid.add(new ArrayList<>(height));
			for (int y = 0; y < height; y++) {
				grid.get(x).add(0);
			}
		}
	}
	
	@Override
	public void paint(Graphics graphics) {
		Graphics2D g2D = (Graphics2D) graphics;
		
		g2D.setColor(new Color(0x0));
		g2D.setFont(new Font("Arial", Font.PLAIN, 25));
		g2D.fillRect(0, 0, 500, 500);
		
		for (int x = 0; x < grid.size(); x++) {
			for (int y = 0; y < grid.get(x).size(); y++) {
				int number = grid.get(x).get(y);
				switch (number) {
					case 0 -> {
						g2D.setColor(new Color(0x232323));
						scaledRect(g2D, x, y);
					}
					case -1 -> {
						g2D.setColor(new Color(0xff0000));
						scaledRect(g2D, x, y);
					}
					default -> {
						g2D.setColor(new Color(0x232323));
						scaledRect(g2D, x, y);
						g2D.setColor(new Color(0x0));
						drawStringInGrid(g2D, Integer.toString(number), x, y);
					}
				}
			}
		}
	}
	
	private void scaledRect(Graphics2D g2D, int x, int y) {
		g2D.fillRect((int) (x * scale.x + 5), (int) (y * scale.y + 5), (int) (scale.x - 10), (int) (scale.y - 10));
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
