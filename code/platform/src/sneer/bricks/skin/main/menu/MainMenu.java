package sneer.bricks.skin.main.menu;

import javax.swing.JComponent;

import sneer.bricks.skin.menu.Menu;
import sneer.bricks.software.bricks.snappstarter.Snapp;
import sneer.foundation.brickness.Brick;

@Snapp
@Brick
public interface MainMenu{

	Menu getSneerMenu();

	JComponent getWidget();

}