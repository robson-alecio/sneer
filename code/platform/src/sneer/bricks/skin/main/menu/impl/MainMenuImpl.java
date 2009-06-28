package sneer.bricks.skin.main.menu.impl;

import static sneer.foundation.environments.Environments.my;

import javax.swing.JComponent;

import sneer.bricks.skin.main.menu.MainMenu;
import sneer.bricks.skin.menu.Menu;
import sneer.bricks.skin.menu.MenuFactory;

class MainMenuImpl implements MainMenu {

	private static final long serialVersionUID = 1L;

	private MenuFactory menuFactory = my(MenuFactory.class);
	
	private static transient Menu sneerMenu;
	private static boolean initializaded = false;

	private final Menu _delegate;
	
	MainMenuImpl(){
		_delegate = menuFactory.createMenuBar();
	}
	
	public void initialize() {
		initializaded = true;
		sneerMenu = menuFactory.createMenuGroup("Menu");
		_delegate.addGroup(sneerMenu);
	}

	public Menu getSneerMenu() {
		synchronized (this) {
			if(!initializaded){
				initialize();
			}
			return sneerMenu;
		}
	}

	@Override
	public JComponent getWidget() {
		return _delegate.getWidget();
	}
}