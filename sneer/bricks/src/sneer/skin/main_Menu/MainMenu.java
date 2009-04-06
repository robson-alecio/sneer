package sneer.skin.main_Menu;

import javax.swing.JComponent;

import sneer.brickness.Brick;
import sneer.skin.menu.Menu;

@Brick
public interface MainMenu{

	Menu<JComponent> getSneerMenu();

	JComponent getWidget();

}