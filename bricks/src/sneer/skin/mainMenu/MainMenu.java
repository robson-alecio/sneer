package sneer.skin.mainMenu;

import javax.swing.JComponent;

import sneer.skin.menu.Menu;

public interface MainMenu {

	Menu<JComponent> getSneerMenu();

	Menu<JComponent> getLookAndFeelMenu();
	
	Menu<JComponent> getPreferencesMenu();

	JComponent getWidget();

}