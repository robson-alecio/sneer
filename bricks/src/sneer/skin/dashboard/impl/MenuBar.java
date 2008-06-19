package sneer.skin.dashboard.impl;

import javax.swing.JComponent;
import javax.swing.JMenuBar;

public class MenuBar extends AbstractSwingMenu {

	private static final long serialVersionUID = 1L;
	protected JMenuBar bar = new JMenuBar();

	@Override
	public void addSeparator() {
		//ignore
	}

	@Override
	protected JComponent getSwingWidget() {
		return bar;
	}
}
