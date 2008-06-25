package sneer.skin.laf.impl;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jvnet.substance.SubstanceLookAndFeel;

import sneer.lego.Inject;
import sneer.skin.dashboard.Dashboard;
import sneer.skin.mainMenu.MainMenu;
import sneer.skin.menu.Menu;
import sneer.skin.menu.MenuFactory;
import wheel.io.ui.Action;

public class AbstractLafSupportImpl {

	@Inject
	protected static MainMenu mainMenu;
	
	@Inject
	protected static MenuFactory<JComponent> menuFactory;
	
	@Inject
	protected static Dashboard dashboard;

	private Action action;
	
	public AbstractLafSupportImpl(String name) {
		try {
			LookAndFeel laf = (LookAndFeel) Class.forName(name).newInstance();
			initAction(laf);
		} catch (Exception e) {
			//do not add a action
		}
	}
	
	public AbstractLafSupportImpl(final LookAndFeel laf) {
		initAction(laf);
	}
	
	public AbstractLafSupportImpl(final List<SubstanceLookAndFeel> laf, String group) {
		Menu<JComponent> menu = menuFactory.createMenuGroup(group);
		mainMenu.getLookAndFeelMenu().addGroup(menu);
		for (LookAndFeel lookAndFeel : laf) {
			initAction(lookAndFeel, menu);
		}
	}

	private void initAction(final LookAndFeel laf) {
		initAction(laf,mainMenu.getLookAndFeelMenu());
	}	

	private void initAction(final LookAndFeel laf, Menu<JComponent> menu) {
		action = new Action(){
			@Override
			public String caption() {
				return laf.getName();
			}

			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(laf);
					dashboard.refreshLaf();
				} catch (UnsupportedLookAndFeelException e) {
					// ignore: same L&F
				}
			}
		};
		menu.addAction(action);
	}

	public Action getAction() {
		return action;
	}
}