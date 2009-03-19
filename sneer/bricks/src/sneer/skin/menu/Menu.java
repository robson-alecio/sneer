package sneer.skin.menu;

import wheel.io.ui.action.Action;

public interface Menu<WIDGET> {

	WIDGET getWidget();
	
	void addAction(Action action);

	void addGroup(Menu<WIDGET> group);
	
	void addSeparator();
	
	void clearAll();

}