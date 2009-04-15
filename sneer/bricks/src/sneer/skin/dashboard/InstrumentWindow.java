package sneer.skin.dashboard;

import java.awt.Container;

import javax.swing.JPopupMenu;

public interface InstrumentWindow {

	Container contentPane();
	JPopupMenu actions();
}