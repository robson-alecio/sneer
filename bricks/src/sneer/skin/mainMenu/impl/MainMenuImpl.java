package sneer.skin.mainMenu.impl;

import javax.swing.JComponent;

import sneer.lego.Inject;
import sneer.skin.mainMenu.MainMenu;
import sneer.skin.menu.Menu;
import sneer.skin.menu.MenuFactory;
import sneer.skin.menu.impl.MenuBar;

public class MainMenuImpl extends MenuBar implements MainMenu{

	private static final long serialVersionUID = 1L;

	@Inject
	static private MenuFactory<JComponent> menuFactory;
	
	private static transient Menu<JComponent> sneerMenu;
	private static boolean initializaded = false;
	
	public void initialize() {
		initializaded = true;
		sneerMenu = menuFactory.createMenuGroup("Menu");
		addGroup(sneerMenu);
	}

	public Menu<JComponent> getSneerMenu() {
		synchronized (this) {
			if(!initializaded){
				initialize();
			}
			return sneerMenu;
		}
	}
}