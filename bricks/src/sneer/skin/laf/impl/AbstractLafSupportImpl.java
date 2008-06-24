package sneer.skin.laf.impl;

import javax.swing.LookAndFeel;

import sneer.lego.Inject;
import sneer.skin.dashboard.Dashboard;
import sneer.skin.laf.LafAction;
import sneer.skin.mainMenu.MainMenu;

public class AbstractLafSupportImpl {

	@Inject
	protected static MainMenu mainMenu;
	@Inject
	protected static Dashboard dashboard;
	
	protected LafAction action;

	public AbstractLafSupportImpl(LookAndFeel laf) {
		action = new LafAction(laf, mainMenu.getLookAndFeelMenu(), dashboard);
	}

	public LafAction getAction() {
		return action;
	}

}