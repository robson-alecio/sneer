package sneer.skin.menu;

import sneer.brickness.Brick;

public interface MenuFactory<WIDGET> extends Brick {

	Menu<WIDGET> createMenuBar();

	Menu<WIDGET> createMenuGroup(String name);
}