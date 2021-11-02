package io.github.leo848;

import java.awt.*;

import static io.github.leo848.Constants.GAME_HEIGHT;
import static io.github.leo848.Constants.WINDOW_HEIGHT;
import static io.github.leo848.Constants.WINDOW_WIDTH;

public class Display {
	public String text;
	
	private Color color;
	private Color backgroundColor;
	
	public Display(String text) {
		this.text = text;
		this.color = new Color(0);
		this.backgroundColor = new Color(0xffffff);
	}
	
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void show(Graphics2D g2D) {
		g2D.setColor(backgroundColor);
		g2D.fillRect(0, GAME_HEIGHT, WINDOW_WIDTH, WINDOW_HEIGHT - GAME_HEIGHT);
		
		g2D.setColor(color);
		g2D.drawString(text, 20, GAME_HEIGHT + (WINDOW_HEIGHT - GAME_HEIGHT) / 2);
	}
}
