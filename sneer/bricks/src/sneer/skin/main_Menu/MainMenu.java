package sneer.skin.main_Menu;

import javax.swing.JComponent;

import sneer.brickness.OldBrick;
import sneer.skin.menu.Menu;

public interface MainMenu extends OldBrick {

	Menu<JComponent> getSneerMenu();

	JComponent getWidget();

}