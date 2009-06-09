package sneer.skin.menu;

import sneer.hardware.gui.Action;

public interface Menu<WIDGET> {

	WIDGET getWidget();
	
	void addAction(Action action);

	void addGroup(Menu<WIDGET> group);
	
	void addSeparator();
	
	void clearAll();

}