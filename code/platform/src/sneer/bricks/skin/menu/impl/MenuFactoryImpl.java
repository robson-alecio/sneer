package sneer.bricks.skin.menu.impl;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;

import sneer.bricks.skin.menu.MenuFactory;
import sneer.bricks.skin.menu.MenuGroup;

class MenuFactoryImpl implements MenuFactory {

	@Override
	public MenuGroup<JMenuBar> createMenuBar(){
		return new MenuBarImpl();
	}
	
	@Override
	public MenuGroup<JMenu> createMenuGroup(String name){
		return new MenuGroupImpl(name);
	}

	@Override
	public MenuGroup<JPopupMenu> createPopupMenu() {
		return new MenuPopupImpl();
	}
}
