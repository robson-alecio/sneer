package sneer.bricks.skin.menu.impl;

import sneer.bricks.skin.menu.Menu;
import sneer.bricks.skin.menu.MenuFactory;

class MenuFactoryImpl implements MenuFactory {

	public Menu createMenuBar(){
		return new MenuBar();
	}
	
	public Menu createMenuGroup(String name){
		return new MenuGroup(name);
	}
	
}
