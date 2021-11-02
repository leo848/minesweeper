package io.github.leo848;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.awt.Font.*;

public final class Constants {
	public static final JSONObject data = loadJSON();
	public static final boolean UPDATE_NEARBY_MINES = data.getBoolean("updateNearbyMines");
	
	// user flags
	
	public static final int NUMBER_OF_MINES = data.getInt("numberOfMines");
	
	public static final int WINDOW_WIDTH = data.getJSONObject("windowSize")
	                                           .getInt("x");
	public static final int WINDOW_HEIGHT = data.getJSONObject("windowSize")
	                                            .getInt("y");
	
	public static final int GAME_WIDTH = data.getJSONObject("gameSize")
	                                         .getInt("x");
	public static final int GAME_HEIGHT = data.getJSONObject("gameSize")
	                                          .getInt("y");
	
	public static final int GRID_WIDTH = data.getJSONObject("gridSize")
	                                         .getInt("x");
	public static final float SCALE_WIDTH = (float) GAME_WIDTH / GRID_WIDTH;
	
	public static final int GRID_HEIGHT = data.getJSONObject("gridSize")
	                                          .getInt("y");
	public static final float SCALE_HEIGHT = (float) GAME_HEIGHT / GRID_HEIGHT;
	
	// calculated stuff
	public static final int TILES = GRID_HEIGHT * GRID_WIDTH;
	public static final Font DEFAULT_FONT = new Font("Arial", PLAIN, 25);
	// Messages
	
	private static final JSONObject messages = data.getJSONObject("messages");
	public static final String MSG_TUTORIAL = messages.getString("tutorial");
	public static final String MSG_AFTER_TUTORIAL = messages.getString("afterTutorial");
	public static final String MSG_GAME_OVER = messages.getString("gameOver");
	public static final String MSG_SOLVED = messages.getString("solved");
	
	private Constants() {}
	
	private static @NotNull JSONObject loadJSON() {
		try {
			String fileData = Files.readString(Paths.get("data.json"));
			return new JSONObject(fileData);
		} catch (IOException e) {
			return new JSONObject();
		}
	}
}
