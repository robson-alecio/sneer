package sneer.skin.laf.motif.impl;

import sneer.lego.Inject;
import sneer.skin.laf.LafManager;
import sneer.skin.laf.motif.MotifLafSupport;
import wheel.io.ui.Action;

public class MotifLafSupportImpl implements MotifLafSupport {

	@Inject
	static private LafManager lafManager;
	
	private Action action;

	public MotifLafSupportImpl(){
		action = lafManager.registerLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
	}

	public Action getAction() {
		return action;
	}

	@Override
	public void setLastUsedAction(Action last) {
		//ignore
	}
}