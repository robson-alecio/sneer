package sneer.skin.main_Menu;

import javax.swing.JComponent;

import sneer.skin.menu.Menu;

public interface MainMenu {

	Menu<JComponent> getSneerMenu();

	JComponent getWidget();

}