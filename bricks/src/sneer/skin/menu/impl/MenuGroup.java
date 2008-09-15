package sneer.skin.menu.impl;

import javax.swing.JComponent;
import javax.swing.JMenu;

class MenuGroup extends AbstractSwingMenu {

	private static final long serialVersionUID = 1L;
	protected JMenu menu = new JMenu();
	
	MenuGroup(String text) {
		menu.setText(text);
	}

	@Override
	public void addSeparator() {
		menu.addSeparator();
	}

	@Override
	public JComponent getWidget() {
		return menu;
	}
}