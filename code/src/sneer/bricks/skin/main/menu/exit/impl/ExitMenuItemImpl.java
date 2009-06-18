package sneer.bricks.skin.main.menu.exit.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.gui.Action;
import sneer.bricks.skin.main.menu.MainMenu;
import sneer.bricks.skin.main.menu.exit.ExitMenuItem;

public class ExitMenuItemImpl implements ExitMenuItem{
	{
		Action cmd = new Action(){
			@Override public String caption() { return "Exit"; }
			@Override public void run() {	System.exit(0);
		}};
		my(MainMenu.class).getSneerMenu().addAction(cmd);
	}
}
