package sneer.bricks.skin.menu.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import sneer.bricks.hardware.gui.Action;
import sneer.bricks.skin.main.synth.menu.SynthMenus;
import sneer.bricks.skin.menu.MenuGroup;

public abstract class AbstractMenuGroup<T extends JComponent> implements MenuGroup<T> {

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
		JMenuItem menuItem = my(SynthMenus.class).createMenuItem();
		menuItem.setText(action.caption());
		addMenuItem(action, menuItem);
	}

	@Override
	public void addGroup(MenuGroup<? extends JComponent> group) {
		getWidget().add(group.getWidget());
	}
	
	private void addMenuItem(final Action action, final JMenuItem menuItem) {
		menuItem.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent ignored) {
			action.run();
			menuItem.setText(action.caption());
		}});
		getWidget().add(menuItem);
	}
}