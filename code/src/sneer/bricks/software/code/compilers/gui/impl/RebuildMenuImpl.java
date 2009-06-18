package sneer.bricks.software.code.compilers.gui.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.gui.Action;
import sneer.bricks.skin.main.menu.MainMenu;
import sneer.bricks.software.code.compilers.gui.RebuildMenu;

class RebuildMenuImpl implements RebuildMenu {

	{
		Action cmd = new Action(){
			@Override public String caption() { return "Rebuild";	}
			@Override	public void run() { rebuild(); }
		};
		my(MainMenu.class).getSneerMenu().addAction(cmd);		
	}

	private void rebuild() {
		throw new sneer.foundation.lang.exceptions.NotImplementedYet(); // Implement
	}
}
