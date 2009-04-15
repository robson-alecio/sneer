package sneer.skin.main.dashboard.impl;

import java.awt.Container;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import sneer.skin.main.dashboard.InstrumentWindow;

public class InstrumentWindowImpl  extends JPanel implements InstrumentWindow{

	private final String _title;

	public InstrumentWindowImpl(String title) {
		_title = title;
		_title.toString();  //Delete this line.
	}

	@Override
	public JPopupMenu actions() {
		throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
	}

	@Override
	public Container contentPane() {
		throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
	}

	public void resizeContents() {
		throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
	}

}
