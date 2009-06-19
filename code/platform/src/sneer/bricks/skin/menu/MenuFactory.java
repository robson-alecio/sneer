package sneer.bricks.skin.menu;

import sneer.foundation.brickness.Brick;

@Brick
public interface MenuFactory<WIDGET> {

	Menu<WIDGET> createMenuBar();

	Menu<WIDGET> createMenuGroup(String name);
}