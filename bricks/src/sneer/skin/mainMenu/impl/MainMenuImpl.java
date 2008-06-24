package sneer.skin.mainMenu.impl;

import javax.swing.JComponent;
import javax.swing.JLabel;

import sneer.bricks.threadpool.ThreadPool;
import sneer.lego.Inject;
import sneer.skin.image.DefaultIcons;
import sneer.skin.image.ImageFactory;
import sneer.skin.mainMenu.MainMenu;
import sneer.skin.menu.Menu;
import sneer.skin.menu.MenuFactory;
import sneer.skin.menu.impl.MenuBar;

public class MainMenuImpl extends MenuBar implements Runnable, MainMenu{

	private static final long serialVersionUID = 1L;

	@Inject
	static private MenuFactory<JComponent> menuFactory;
	
	@Inject
	static private ImageFactory imageFactory;
	
	@Inject
	static private ThreadPool threadPool;
	
	private transient Menu<JComponent> sneerMenu;
	private transient Menu<JComponent> lookAndFeelMenu;

	protected MainMenuImpl() {
		threadPool.registerActor(this);
	}
	
	public void initialize() {
		getWidget().add(new JLabel(imageFactory.getIcon(DefaultIcons.logoTray)));
		
		sneerMenu = menuFactory.createMenuGroup("Menu");
		addGroup(sneerMenu);
		
		lookAndFeelMenu = menuFactory.createMenuGroup("Look&Feel");
		addGroup(lookAndFeelMenu);
	}

	public Menu<JComponent> getSneerMenu() {
		return sneerMenu;
	}
	
	public Menu<JComponent> getLookAndFeelMenu() {
		return lookAndFeelMenu;
	}

	@Override
	public void run() {
		initialize();
	}
}