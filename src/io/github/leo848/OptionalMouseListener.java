package io.github.leo848;

import java.awt.event.*;

public interface OptionalMouseListener extends MouseListener {
	@Override
	default void mouseClicked(MouseEvent e) {
	}
	
	@Override
	default void mousePressed(MouseEvent e) {
	}
	
	@Override
	default void mouseReleased(MouseEvent e) {
	
	}
	
	@Override
	default void mouseEntered(MouseEvent e) {
	}
	
	@Override
	default void mouseExited(MouseEvent e) {
	}
}
