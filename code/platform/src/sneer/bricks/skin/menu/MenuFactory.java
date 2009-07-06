package sneer.bricks.skin.menu;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;

import sneer.foundation.brickness.Brick;

@Brick
public interface MenuFactory {

	MenuGroup<JMenuBar> createMenuBar();
	MenuGroup<JMenu> createMenuGroup(String name);
	MenuGroup<JPopupMenu> createPopupMenu();
}