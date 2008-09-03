package sneer.skin.menu.impl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenuItem;

import sneer.skin.menu.Menu;
import wheel.io.ui.action.Action;
import wheel.io.ui.action.ReactiveAction;
import wheel.io.ui.action.SelectableAction;

public abstract class AbstractSwingMenu implements Menu<JComponent>{
	
	@Override
	public void addAction(final Action action) {
		
		JMenuItem menuItem;
		if (action instanceof SelectableAction) {
			class SneerCheckBoxMenuItem extends JCheckBoxMenuItem{
				
				private static final long serialVersionUID = 1L;
				
				public SneerCheckBoxMenuItem(String caption, boolean selected) {
					super(caption,selected);
				}

				@Override
				public boolean getState() {
					setState(((SelectableAction)action).isSelected());
					return super.getState();
				}
			}
			
			menuItem = new SneerCheckBoxMenuItem(action.caption(),((SelectableAction)action).isSelected());
			
		}else{
			if (action instanceof ReactiveAction) {
				menuItem = new ReactiveMenuItem(((ReactiveAction)action).signalCaption());
			}else{
				menuItem = new JMenuItem(action.caption());
			}
		}
		addMenuItem(action, menuItem);
	}

	private void addMenuItem(final Action action,	final JMenuItem menuItem) {
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

	public AbstractSwingMenu() {
		super();
		// Implement Auto-generated constructor stub
	}
}