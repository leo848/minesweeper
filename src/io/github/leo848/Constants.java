package io.github.leo848;

import java.awt.*;

import static java.awt.Font.*;

public final class Constants {
	public static final int NUMBER_OF_MINES = 40;
	
	public static final int WINDOW_HEIGHT = 500;
	public static final int WINDOW_WIDTH = 500;
	public static final int GRID_HEIGHT = 16;
	public static final int GRID_WIDTH = 16;
	public static final Font DEFAULT_FONT = new Font("Arial", PLAIN, 25);
	
	public static final int SCALE_HEIGHT = WINDOW_HEIGHT / GRID_HEIGHT;
	public static final int SCALE_WIDTH = WINDOW_WIDTH / GRID_WIDTH;
	public static final Vector SCALE_VECTOR = new Vector(SCALE_WIDTH, SCALE_HEIGHT);
	public static final int TILES = GRID_HEIGHT * GRID_WIDTH;
	
	private Constants() {
	}
}
