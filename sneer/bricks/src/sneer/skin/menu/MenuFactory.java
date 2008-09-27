package sneer.skin.menu;

public interface MenuFactory<WIDGET> {

	Menu<WIDGET> createMenuBar();

	Menu<WIDGET> createMenuGroup(String name);
}