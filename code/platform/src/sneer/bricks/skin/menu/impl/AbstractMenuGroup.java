package sneer.bricks.skin.menu.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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
		final JMenuItem menuItem = my(SynthMenus.class).createMenuItem();
		addMenuItem(action, menuItem);
		menuItem.setText(action.caption());
		menuItem.addPropertyChangeListener(new PropertyChangeListener(){ @Override public void propertyChange(PropertyChangeEvent evt) {
			menuItem.setText(action.caption());
		}});
	}

	@Override
	public void addGroup(MenuGroup<? extends JComponent> group) {
		getWidget().add(group.getWidget());
	}
	
	private void addMenuItem(final Action action, final JMenuItem menuItem) {
		menuItem.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent ignored) {
			action.run();
		}});
		getWidget().add(menuItem);
	}
}