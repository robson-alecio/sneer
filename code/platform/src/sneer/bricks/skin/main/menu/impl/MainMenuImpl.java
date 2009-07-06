package sneer.bricks.skin.main.menu.impl;

import static sneer.foundation.environments.Environments.my;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import sneer.bricks.hardware.gui.Action;
import sneer.bricks.skin.main.menu.MainMenu;
import sneer.bricks.skin.main.synth.Synth;
import sneer.bricks.skin.menu.MenuFactory;
import sneer.bricks.skin.menu.MenuGroup;

class MainMenuImpl implements MainMenu {

	private final MenuGroup<JMenuBar> _menuBar = my(MenuFactory.class).createMenuBar();
	private MenuGroup<JMenu> _delegate;
	
	MainMenuImpl(){
		my(Synth.class).loadForWussies(this.getClass());
	}
	
	@Override public JMenuBar getMenuBarWidget() {
		return _menuBar.getWidget();
	}
	
	@Override public void addAction(Action action) { delegate().addAction(action); }
	@Override public void addAction(String caption, Runnable action) { delegate().addAction(caption, action); }
	@Override public void addGroup(MenuGroup<? extends JComponent> group) { delegate().addGroup(group); }
	@Override public JMenu getWidget() { return delegate().getWidget(); }
	
	private synchronized MenuGroup<JMenu> delegate() {
		if (_delegate == null) initMenu();
		return _delegate;
	}

	private void initMenu() {
		_delegate = my(MenuFactory.class).createMenuGroup("Menu");
		_delegate.getWidget().setName("MainMenu");
		_menuBar.addGroup(_delegate);
	}
}