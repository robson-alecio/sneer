package sneer.bricks.skin.menu;

import javax.swing.JComponent;

import sneer.bricks.hardware.gui.Action;

public interface Menu {

	JComponent getWidget();
	
	void addAction(Action action);
	void addAction(String caption, Runnable action);

	void addGroup(Menu group);
	

}