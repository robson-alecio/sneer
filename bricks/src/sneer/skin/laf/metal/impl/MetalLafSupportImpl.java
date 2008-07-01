package sneer.skin.laf.metal.impl;

import javax.swing.plaf.metal.MetalLookAndFeel;

import sneer.lego.Inject;
import sneer.skin.laf.LafManager;
import sneer.skin.laf.metal.MetalLafSupport;
import wheel.io.ui.action.Action;

public class MetalLafSupportImpl implements MetalLafSupport {

	@Inject
	static private LafManager lafManager;
	
	private Action action;

	public MetalLafSupportImpl(){
		action = lafManager.registerLookAndFeel(new MetalLookAndFeel());
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