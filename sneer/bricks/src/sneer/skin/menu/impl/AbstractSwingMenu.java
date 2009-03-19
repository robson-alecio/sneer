package sneer.skin.menu.impl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import sneer.skin.menu.Menu;
import wheel.io.ui.action.Action;

abstract class AbstractSwingMenu implements Menu<JComponent> {

	@Override
	public void addAction(final Action action) {
		addMenuItem(action, new JMenuItem(action.caption()));
	}


	private void addMenuItem(final Action action, final JMenuItem menuItem) {
		menuItem.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent ignored) {
			action.run();
			menuItem.setText(action.caption());
		}});
		getWidget().add(menuItem);
	}

	@Override
	public void addGroup(Menu<JComponent> group) {
		getWidget().add(group.getWidget());
	}

	@Override
	public void clearAll() {
		getWidget().removeAll();
	}
}