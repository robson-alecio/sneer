package sneer.bricks.skin.widgets.autoscroll;

import javax.swing.JScrollPane;

import sneer.foundation.brickness.Brick;

@Brick
public interface AutoScroll {
	
	void runWithAutoscroll(JScrollPane scrollPane, Runnable runnable);

}