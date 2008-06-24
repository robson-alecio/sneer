package sneer.skin.laf.napkin.impl;

import net.sourceforge.napkinlaf.NapkinLookAndFeel;
import sneer.lego.Inject;
import sneer.skin.dashboard.Dashboard;
import sneer.skin.laf.LafAction;
import sneer.skin.laf.napkin.NapkinLafSupport;
import sneer.skin.mainMenu.MainMenu;


public class NapkinLafSupportImpl implements NapkinLafSupport{

	@Inject
	private static MainMenu mainMenu;
	
	@Inject
	private static Dashboard dashboard;
	
	private LafAction action;
	
	public NapkinLafSupportImpl(){
		action = new LafAction(new NapkinLookAndFeel(), 
						  		   mainMenu.getLookAndFeelMenu(), 
						  		   dashboard);
	}

	public LafAction getAction() {
		return action;
	}
}