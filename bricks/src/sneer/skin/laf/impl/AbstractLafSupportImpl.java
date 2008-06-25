package sneer.skin.laf.impl;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import sneer.lego.Inject;
import sneer.skin.dashboard.Dashboard;
import sneer.skin.mainMenu.MainMenu;
import wheel.io.ui.Action;

public class AbstractLafSupportImpl {

	@Inject
	protected static MainMenu mainMenu;
	
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

	private void initAction(final LookAndFeel laf) {
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
		mainMenu.getLookAndFeelMenu().addAction(action);
	}

	public Action getAction() {
		return action;
	}
}