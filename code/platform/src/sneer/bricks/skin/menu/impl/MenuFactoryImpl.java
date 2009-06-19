package sneer.bricks.skin.menu.impl;

import javax.swing.JComponent;

import sneer.bricks.skin.menu.Menu;
import sneer.bricks.skin.menu.MenuFactory;

class MenuFactoryImpl implements MenuFactory<JComponent> {

	public Menu<JComponent> createMenuBar(){
		return new MenuBar();
	}
	
	public Menu<JComponent> createMenuGroup(String name){
		return new MenuGroup(name);
	}
	
}
