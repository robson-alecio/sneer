package sneer.skin.menu.impl;

import javax.swing.JComponent;
import javax.swing.JMenuBar;


public class MenuBar extends AbstractSwingMenu {

	private static final long serialVersionUID = 1L;
	protected JMenuBar bar = new JMenuBar();

	protected MenuBar() {}

	@Override
	public void addSeparator() {
		//ignore
	}

	@Override
	public JComponent getWidget() {
		return bar;
	}
}
