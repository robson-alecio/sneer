package sneer.bricks.skin.menu.impl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import sneer.bricks.hardware.gui.Action;
import sneer.bricks.skin.menu.Menu;

abstract class AbstractSwingMenu implements Menu {

	@Override
	public void addAction(final String caption, final Runnable delegate) {
		addAction(new Action(){
			@Override
			public String caption() {
				return caption;
			}

			@Override
			public void run() {
				delegate.run();
			}
		});
	}

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
	public void addGroup(Menu group) {
		getWidget().add(group.getWidget());
	}

}