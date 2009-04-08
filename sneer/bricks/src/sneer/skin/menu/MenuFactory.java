package sneer.skin.menu;

import sneer.brickness.Brick;

@Brick
public interface MenuFactory<WIDGET> {

	Menu<WIDGET> createMenuBar();

	Menu<WIDGET> createMenuGroup(String name);
}