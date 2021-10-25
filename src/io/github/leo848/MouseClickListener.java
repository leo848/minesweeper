package io.github.leo848;

import java.awt.event.*;

public interface MouseClickListener extends MouseListener {
	@Override
	default void mouseClicked(MouseEvent e) {
	}
	
	@Override
	default void mousePressed(MouseEvent e) {
	}
	
	@Override
	void mouseReleased(MouseEvent e);
	
	@Override
	default void mouseEntered(MouseEvent e) {
	}
	
	@Override
	default void mouseExited(MouseEvent e) {
	}
}
