package sneer.skin.menu.impl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenuItem;

import sneer.skin.menu.Menu;
import wheel.io.ui.action.Action;
import wheel.io.ui.action.SelectableAction;

abstract class AbstractSwingMenu implements Menu<JComponent> {

	@Override
	public void addAction(final Action action) {
		addMenuItem(action, new JMenuItem(action.caption()));
	}

	@Override
	public void addAction(final SelectableAction action) {
		JMenuItem menuItem;
		class SneerCheckBoxMenuItem extends JCheckBoxMenuItem {
			private static final long serialVersionUID = 1L;
			@Override 	public boolean getState() {
				setState(action.isSelected());
				return super.getState();
			}
		}

		menuItem = new SneerCheckBoxMenuItem();
		menuItem.setText(action.caption());
		menuItem.setSelected(action.isSelected());
		addMenuItem(action, menuItem);
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