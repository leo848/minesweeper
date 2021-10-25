package io.github.leo848;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

public class MinesweeperCanvas extends JPanel {
	final Vector scale = new Vector();
	Vector size = new Vector(500, 500);
	List<List<Integer>> grid;
	private GameLoop gameLoop;
	private Random random = new Random();
	
	public MinesweeperCanvas(GameLoop gameLoop) {
		this.gameLoop = gameLoop;
		
		setPreferredSize(new Dimension((int) size.x, (int) size.y));
		scale.set(size.x / 10, size.y / 10);
		
		fillGrid(10, 10);
		distributeMines(10);
		calculateNeighbors();
	}
	
	private void calculateNeighbors() {
		for (int x = 0; x < grid.size(); x++) {
			for (int y = 0; y < grid.get(x).size(); y++) {
				if (grid.get(x).get(y) == -1) continue;
				
				int i = 0; // neighbor count
				i += safeArrayListCheck(x - 1, y - 1, -1);
				i += safeArrayListCheck(x, y - 1, -1);
				i += safeArrayListCheck(x + 1, y - 1, -1);
				
				i += safeArrayListCheck(x - 1, y, -1);
				i += safeArrayListCheck(x + 1, y, -1);
				
				i += safeArrayListCheck(x - 1, y + 1, -1);
				i += safeArrayListCheck(x, y + 1, -1);
				i += safeArrayListCheck(x + 1, y + 1, -1);
				
				grid.get(x).set(y, i);
			}
		}
	}
	
	private int safeArrayListCheck(int x, int y, Integer check) {
		int flag = 0;
		try {
			return grid.get(x).get(y).equals(check) ? 1 : 0;
		} catch (IndexOutOfBoundsException e) {
			return 0;
		}
	}
	
	@Override
	public void paint(Graphics graphics) {
		Graphics2D g2D = (Graphics2D) graphics;
		
		g2D.setColor(new Color(0xffffff));
		g2D.setFont(new Font("Arial", Font.PLAIN, 50));
		g2D.fillRect(0, 0, 500, 500);
		
		g2D.setColor(new Color(0xffffff));
		for (int x = 0; x < grid.size(); x++) {
			for (int y = 0; y < grid.get(x).size(); y++) {
				int number = grid.get(x).get(y);
				if (number == -1) {
					g2D.setColor(new Color(0xff0000));
					g2D.fillRect((int) (x * scale.x + 5), (int) (y * scale.y + 5), (int) (scale.x - 10), (int) (scale.y - 10));
				} else {
					g2D.setColor(new Color(0xffffff));
					g2D.fillRect((int) (x * scale.x), (int) (y * scale.y), (int) scale.x, (int) scale.y);
					g2D.setColor(new Color(0x0));
					g2D.drawString(Integer.toString(number), x * scale.x, (y+1) * scale.y);
				}
			}
		}
	}
	
	public void distributeMines(int amount) {
		List<Integer> possibilities = IntStream
				.range(0, grid.size() * grid.get(0).size())
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
}
