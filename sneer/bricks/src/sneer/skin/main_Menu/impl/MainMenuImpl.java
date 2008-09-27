package sneer.skin.main_Menu.impl;

import javax.swing.JComponent;

import sneer.kernel.container.Inject;
import sneer.skin.main_Menu.MainMenu;
import sneer.skin.menu.Menu;
import sneer.skin.menu.MenuFactory;

class MainMenuImpl implements MainMenu{

	private static final long serialVersionUID = 1L;

	@Inject
	static private MenuFactory<JComponent> menuFactory;
	
	private static transient Menu<JComponent> sneerMenu;
	private static boolean initializaded = false;

	private final Menu<JComponent> _delegate;
	
	MainMenuImpl(){
		_delegate = menuFactory.createMenuBar();
	}
	
	public void initialize() {
		initializaded = true;
		sneerMenu = menuFactory.createMenuGroup("Menu");
		_delegate.addGroup(sneerMenu);
	}

	public Menu<JComponent> getSneerMenu() {
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