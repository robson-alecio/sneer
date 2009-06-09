package sneer.bricks.skin.main.menu;

import javax.swing.JComponent;

import sneer.bricks.skin.menu.Menu;
import sneer.foundation.brickness.Brick;

@Brick
public interface MainMenu{

	Menu<JComponent> getSneerMenu();

	JComponent getWidget();

}