package sneer.skin.main_Menu;

import javax.swing.JComponent;

import sneer.brickness.Brick;
import sneer.skin.menu.Menu;

public interface MainMenu extends Brick {

	Menu<JComponent> getSneerMenu();

	JComponent getWidget();

}