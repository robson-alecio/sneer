package sneer.bricks.skin.main.menu;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import sneer.bricks.skin.menu.MenuGroup;
import sneer.bricks.software.bricks.snappstarter.Snapp;
import sneer.foundation.brickness.Brick;

@Snapp
@Brick
public interface MainMenu extends MenuGroup<JMenu> {

	JMenuBar getMenuBarWidget();

}