package io.github.leo848;

import javax.swing.*;

public class MinesweeperFrame extends JFrame {
	MinesweeperCanvas canvas;
	public MinesweeperFrame(GameLoop gameLoop) {
		canvas = new MinesweeperCanvas(gameLoop);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		add(canvas);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void repaintCanvas() {
		canvas.repaint();
	}
}
