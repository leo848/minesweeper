package io.github.leo848;

import java.awt.*;

import static java.awt.Font.*;

public final class Constants {
	public static final int NUMBER_OF_MINES = 40;
	
	public static final int WINDOW_HEIGHT = 600, WINDOW_WIDTH = 500;
	public static final int GAME_HEIGHT = 500, GAME_WIDTH = 500;
	public static final int GRID_WIDTH = 16, GRID_HEIGHT = 16;
	
	public static final boolean UPDATE_NEARBY_MINES = true;
	public static final Font DEFAULT_FONT = new Font("Arial", PLAIN, 25);
	
	public static final float SCALE_HEIGHT = (float) GAME_HEIGHT / GRID_HEIGHT;
	public static final float SCALE_WIDTH = (float) GAME_WIDTH / GRID_WIDTH;
	public static final int TILES = GRID_HEIGHT * GRID_WIDTH;
	
	private Constants() {
	
	}
}
