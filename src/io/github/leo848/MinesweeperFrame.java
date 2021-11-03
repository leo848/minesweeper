package io.github.leo848;

import javax.swing.*;
import java.util.Locale;

public class MinesweeperFrame extends JFrame {
	MinesweeperCanvas canvas;
	
	public MinesweeperFrame(GameLoop gameLoop) {
		Locale.setDefault(Locale.ENGLISH);
		
		canvas = new MinesweeperCanvas(gameLoop);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		add(canvas);
		addMouseListener(canvas);
		addMouseMotionListener(canvas);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void repaintCanvas() {
		canvas.repaint();
	}
}
