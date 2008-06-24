package sneer.skin.laf.metal.impl;

import javax.swing.plaf.metal.MetalLookAndFeel;

import sneer.lego.Inject;
import sneer.skin.dashboard.Dashboard;
import sneer.skin.laf.LafAction;
import sneer.skin.laf.metal.MetalLafSupport;
import sneer.skin.mainMenu.MainMenu;

public class MetalLafSupportImpl implements MetalLafSupport {

	@Inject
	private static MainMenu mainMenu;
	
	@Inject
	private static Dashboard dashboard;
	
	private LafAction action;
	
	public MetalLafSupportImpl(){
		action = new LafAction(new MetalLookAndFeel(), 
						  		   mainMenu.getLookAndFeelMenu(), 
						  		   dashboard);
	}

	public LafAction getAction() {
		return action;
	}

	
}