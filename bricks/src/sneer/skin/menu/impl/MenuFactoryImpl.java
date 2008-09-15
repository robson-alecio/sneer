package sneer.skin.menu.impl;

import javax.swing.JComponent;

import sneer.skin.menu.Menu;
import sneer.skin.menu.MenuFactory;

class MenuFactoryImpl implements MenuFactory<JComponent> {

	public Menu<JComponent> createMenuBar(){
		return new MenuBar();
	}
	
	public Menu<JComponent> createMenuGroup(String name){
		return new MenuGroup(name);
	}
	
}
