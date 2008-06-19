package sneer.skin.dashboard.impl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import wheel.io.ui.Action;

public abstract class AbstractSwingMenu implements Menu{

	protected abstract JComponent getSwingWidget();
	
	@Override
	public void addAction(final Action action) {
		
		final JMenuItem menuItem = new JMenuItem(action.caption());
		menuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ignored) {
				action.run();
				menuItem.setText(action.caption());
			}
		});

		getSwingWidget().add(menuItem);	
	}

	@Override
	public void addGroup(Menu group) {
		getSwingWidget().add(((AbstractSwingMenu)group).getSwingWidget());
	}

	@Override
	public void clearAll() {
		getSwingWidget().removeAll();
	}
}