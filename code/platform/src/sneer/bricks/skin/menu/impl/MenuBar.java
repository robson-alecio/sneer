package sneer.bricks.skin.menu.impl;

import javax.swing.JComponent;
import javax.swing.JMenuBar;

class MenuBar extends AbstractSwingMenu {

	private static final long serialVersionUID = 1L;
	protected JMenuBar bar = new JMenuBar();

	protected MenuBar() {}

	@Override
	public JComponent getWidget() {
		return bar;
	}
}
