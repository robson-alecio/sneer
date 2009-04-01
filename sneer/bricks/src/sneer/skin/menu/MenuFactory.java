package sneer.skin.menu;

import sneer.brickness.OldBrick;

public interface MenuFactory<WIDGET> extends OldBrick {

	Menu<WIDGET> createMenuBar();

	Menu<WIDGET> createMenuGroup(String name);
}