package sneer.skin.menu;

import wheel.io.ui.action.Action;
import wheel.io.ui.action.SelectableAction;

public interface Menu<WIDGET> {

	WIDGET getWidget();
	
	void addAction(Action action);
	void addAction(SelectableAction action);

	void addGroup(Menu<WIDGET> group);
	
	void addSeparator();
	
	void clearAll();

}