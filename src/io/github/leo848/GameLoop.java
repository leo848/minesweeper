package io.github.leo848;

import java.awt.*;
import java.awt.image.*;

public class GameLoop implements Runnable {
	public Thread thread;
	public BufferStrategy bs;
	public Graphics g;
	MinesweeperFrame frame;
	
	int optimalFPS = 60;
	int sleepMillis = 1000 / optimalFPS;
	private Boolean running = false;
	
	public GameLoop() {
	
	}
	
	public synchronized void start() {
		System.out.println("start");
		if (running) return;
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	@SuppressWarnings("BusyWait")
	public void run() {
		System.out.println("run");
		init();
		
		while (running) {
			update();
			render();
			try {
				Thread.sleep(sleepMillis);
			} catch (InterruptedException ignored) {
			
			}
			
		}
		
		stop();
	}
	
	private void init() {
		System.out.println("init");
		frame = new MinesweeperFrame(this);
		
	}
	
	private void update() {
		//System.out.println("update");
		
	}
	
	private void render() {
		frame.repaintCanvas();
	}
	
	public synchronized void stop() {
		if (!running) return;
		running = false;
		frame.dispose();
		try {
			thread.join();
		} catch (Exception ignored) {
		
		}
	}
}