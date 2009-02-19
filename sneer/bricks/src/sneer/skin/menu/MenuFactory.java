package sneer.skin.menu;

import sneer.kernel.container.Brick;

public interface MenuFactory<WIDGET> extends Brick {

	Menu<WIDGET> createMenuBar();

	Menu<WIDGET> createMenuGroup(String name);
}