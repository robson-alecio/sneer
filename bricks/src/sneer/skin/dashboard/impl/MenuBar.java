package sneer.skin.dashboard.impl;

import javax.swing.JComponent;
import javax.swing.JToolBar;

public class MenuBar extends AbstractSwingMenu {

	private static final long serialVersionUID = 1L;
	protected JToolBar bar = new JToolBar();

	@Override
	public void addSeparator() {
		bar.addSeparator();
	}

	@Override
	protected JComponent getSwingWidget() {
		return bar;
	}
}
