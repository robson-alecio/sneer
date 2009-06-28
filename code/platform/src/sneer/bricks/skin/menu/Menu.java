package sneer.bricks.skin.menu;

import sneer.bricks.hardware.gui.Action;

public interface Menu<WIDGET> {

	WIDGET getWidget();
	
	void addAction(Action action);

	void addGroup(Menu<WIDGET> group);
	

}