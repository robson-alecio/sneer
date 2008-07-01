package sneer.skin.laf.so.impl;

import javax.swing.UIManager;

import sneer.lego.Inject;
import sneer.skin.laf.LafManager;
import sneer.skin.laf.so.SOLafSupport;
import wheel.io.ui.Action;

public class SOLafSupportImpl implements SOLafSupport {

	@Inject
	static private LafManager lafManager;
	
	private Action action;

	public SOLafSupportImpl(){
		action = lafManager.registerLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	}

	@Override
	public Action getAction() {
		return action;
	}	
	
	@Override
	public void setLastUsedAction(Action last) {
		//ignore
	}	
}