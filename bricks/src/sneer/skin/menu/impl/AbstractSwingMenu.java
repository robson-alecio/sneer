package sneer.skin.menu.impl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import sneer.skin.menu.Menu;


import wheel.io.ui.Action;

public abstract class AbstractSwingMenu implements Menu<JComponent>{
	
	@Override
	public void addAction(final Action action) {
		
		final JMenuItem menuItem = new JMenuItem(action.caption());
		menuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ignored) {
				action.run();
				menuItem.setText(action.caption());
			}
		});

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