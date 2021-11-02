package io.github.leo848;

import java.awt.*;
import java.util.Objects;

import static io.github.leo848.Constants.GAME_HEIGHT;
import static io.github.leo848.Constants.WINDOW_HEIGHT;
import static io.github.leo848.Constants.WINDOW_WIDTH;

public class Display {
	private final Color backgroundColor;
	public String text;
	Runnable opacityUpdater = () -> {};
	private Color color;
	private int opacity = 254;
	
	public Display(String text) {
		this.text = text;
		this.color = new Color(0);
		this.backgroundColor = new Color(0xffffff);
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
		if (Objects.equals(this.text, text)) return;
		opacityUpdater = () -> {
			opacity--;
			if (opacity == 0 || this.text.isBlank()) {
				this.text = text;
				opacityUpdater = () -> {
					opacity++;
					if (opacity == 254) opacityUpdater = () -> {};
				};
			}
		};
	}
	
	public void show(Graphics2D g2D) {
		opacityUpdater.run();
		
		g2D.setColor(backgroundColor);
		g2D.fillRect(0, GAME_HEIGHT, WINDOW_WIDTH, WINDOW_HEIGHT - GAME_HEIGHT);
		
		g2D.setColor(new Color((color.getRGB() & 0xffffff) | opacity << 24, true));
		g2D.drawString(text, 20, GAME_HEIGHT + (WINDOW_HEIGHT - GAME_HEIGHT) / 2);
	}
}
