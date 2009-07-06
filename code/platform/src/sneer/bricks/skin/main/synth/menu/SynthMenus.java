package sneer.bricks.skin.main.synth.menu;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import sneer.bricks.software.bricks.snappstarter.Snapp;
import sneer.foundation.brickness.Brick;

@Brick
@Snapp
public interface SynthMenus {

	JMenuBar createMenuBar();
	JMenu createMenuGroup();
	JMenuItem createMenuItem();
	JPopupMenu createMenuPopup();

}
