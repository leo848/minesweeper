package io.github.leo848;

import java.awt.*;
import java.util.Objects;

import static io.github.leo848.Constants.GAME_HEIGHT;
import static io.github.leo848.Constants.WINDOW_HEIGHT;
import static io.github.leo848.Constants.WINDOW_WIDTH;

public class Display {
	private final Color backgroundColor;
	private final Color color;
	public String text;
	Runnable updater = () -> {};
	private int opacity = 254;
	
	public Display(String text) {
		this.text = text;
		this.color = new Color(0);
		this.backgroundColor = new Color(0xffffff);
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(final Object object, Animation anim) {
		final String text = String.valueOf(object);
		if (Objects.equals(this.text, text)) return;
		switch (anim) {
			case DIRECT -> {
				this.text = text;
				updater = () -> {};
			}
			case FADE -> updater = () -> {
				opacity--;
				if (opacity == 0 || this.text.isBlank()) {
					this.text = String.valueOf(text);
					updater = () -> {
						opacity++;
						if (opacity == 254) updater = () -> {};
					};
				}
			};
			case TYPING -> updater = () -> {
				if (this.text.isEmpty()) updater = () -> {
					if (Objects.equals(this.text, text)) updater = () -> {};
					else this.text += text.toCharArray()[this.text.length()];
				};
				else this.text = this.text.substring(0, this.text.length() - 1);
			};
		}
	}
	
	public void show(Graphics2D g2D) {
		updater.run(); // yes i don't know anything about oop, how did you know?
		
		g2D.setColor(backgroundColor);
		g2D.fillRect(0, GAME_HEIGHT, WINDOW_WIDTH, WINDOW_HEIGHT - GAME_HEIGHT);
		
		g2D.setColor(new Color((color.getRGB() & 0xffffff) | opacity << 24, true));
		g2D.drawString(text, 20, GAME_HEIGHT + (WINDOW_HEIGHT - GAME_HEIGHT) / 2);
	}
	
	enum Animation {
		DIRECT, TYPING, FADE
	}
}
