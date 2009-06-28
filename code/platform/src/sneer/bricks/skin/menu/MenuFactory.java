package sneer.bricks.skin.menu;

import sneer.foundation.brickness.Brick;

@Brick
public interface MenuFactory {

	Menu createMenuBar();

	Menu createMenuGroup(String name);
}