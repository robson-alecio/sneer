package sneer.bricks.skin.main.menu.impl;

import static sneer.foundation.environments.Environments.my;

import javax.swing.JComponent;

import sneer.bricks.hardware.gui.Action;
import sneer.bricks.skin.main.menu.MainMenu;
import sneer.bricks.skin.menu.Menu;
import sneer.bricks.skin.menu.MenuFactory;

class MainMenuImpl implements MainMenu {

	private final Menu _menuBar = my(MenuFactory.class).createMenuBar();
	private Menu _delegate;

	
	@Override public JComponent getMenuBarWidget() {
		return _menuBar.getWidget();
	}
	
	@Override public void addAction(Action action) { delegate().addAction(action); }
	@Override public void addAction(String caption, Runnable action) { delegate().addAction(caption, action); }
	@Override public void addGroup(Menu group) { delegate().addGroup(group); }
	@Override public JComponent getWidget() { return delegate().getWidget(); }
	

	private synchronized Menu delegate() {
		if (_delegate == null) initMenu();
		return _delegate;
	}

	private void initMenu() {
		_delegate = my(MenuFactory.class).createMenuGroup("Menu");
		_menuBar.addGroup(_delegate);
	}


}