package sneer.skin.mainMenu.impl;

import javax.swing.JComponent;
import javax.swing.JLabel;

import sneer.lego.Inject;
import sneer.skin.image.DefaultIcons;
import sneer.skin.image.ImageFactory;
import sneer.skin.mainMenu.MainMenu;
import sneer.skin.menu.Menu;
import sneer.skin.menu.MenuFactory;
import sneer.skin.menu.impl.MenuBar;

public class MainMenuImpl extends MenuBar implements MainMenu{

	private static final long serialVersionUID = 1L;

	@Inject
	static private MenuFactory<JComponent> menuFactory;
	
	@Inject
	static private ImageFactory imageFactory;
	
	private static transient Menu<JComponent> sneerMenu;
	private static transient Menu<JComponent> windowMenu;
	private static transient Menu<JComponent> lookAndFeelMenu;
	private static transient Menu<JComponent> preferencesMenu;
	private static boolean initializaded = false;
	
	public void initialize() {
		initializaded = true;
		getWidget().add(new JLabel(imageFactory.getIcon(DefaultIcons.logoTray)));
		
		sneerMenu = menuFactory.createMenuGroup("Menu");
		addGroup(sneerMenu);
		
		windowMenu = menuFactory.createMenuGroup("Window");
		addGroup(windowMenu);
		
		lookAndFeelMenu = menuFactory.createMenuGroup("Look&Feel");
		windowMenu.addGroup(lookAndFeelMenu);
		
		preferencesMenu = menuFactory.createMenuGroup("Preferences");
		windowMenu.addGroup(preferencesMenu);
	}

	public Menu<JComponent> getSneerMenu() {
		synchronized (this) {
			if(!initializaded){
				initialize();
			}else{
				try {
					Thread.sleep(10);
					if(sneerMenu==null)
						return getSneerMenu();
				} catch (InterruptedException e) {
					//ignore
				}
			}
		}
		return sneerMenu;
	}
	
	public Menu<JComponent> getLookAndFeelMenu() {
		if(lookAndFeelMenu==null)
			initialize();
		return lookAndFeelMenu;
	}

	@Override
	public Menu<JComponent> getPreferencesMenu() {
		if(preferencesMenu==null)
			initialize();
		return preferencesMenu;
	}
}