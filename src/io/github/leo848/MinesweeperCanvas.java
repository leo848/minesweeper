package io.github.leo848;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class MinesweeperCanvas extends JPanel {
	Vector size = new Vector(500, 500);
	private GameLoop gameLoop;
	
	List<List<Integer>> grid;
	
	public MinesweeperCanvas(GameLoop gameLoop) {
		this.gameLoop = gameLoop;
		
		setPreferredSize(new Dimension((int) size.x, (int) size.y));
		fillGrid(10, 10);
	}
	
	@Override
	public void paint(Graphics graphics) {
		Graphics2D g2D = (Graphics2D) graphics;
		
		g2D.setColor(new Color(0x0));
		g2D.fillRect(0, 0, 500, 500);
		
		
	}
	
	public void fillGrid(int width, int height) {
		for (int x = 0; x < width; x++) {
			grid.set(x, new ArrayList<>(height));
			for (int y = 0; y < height; y++) {
				grid.get(x).set(y, 0);
			}
		}
	}
}
