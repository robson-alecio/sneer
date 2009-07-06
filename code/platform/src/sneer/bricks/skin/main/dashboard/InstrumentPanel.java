package sneer.bricks.skin.main.dashboard;

import java.awt.Container;

import javax.swing.JPopupMenu;

import sneer.bricks.skin.menu.MenuGroup;

public interface InstrumentPanel {

	Container contentPane();
	MenuGroup<JPopupMenu> actions();
}