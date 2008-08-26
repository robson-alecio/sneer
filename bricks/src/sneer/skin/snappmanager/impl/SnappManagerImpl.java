package sneer.skin.snappmanager.impl;

import sneer.skin.snappmanager.SnappManager;
import sneer.skin.viewmanager.Snapp;
import wheel.reactive.lists.ListRegister;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.ListRegisterImpl;

public class SnappManagerImpl implements SnappManager {

	ListRegister<Snapp> _snapps = new ListRegisterImpl<Snapp>();
	
	@Override
	public void registerSnapp(Snapp snapp) {
		_snapps.add(snapp);
	}

	@Override
	public ListSignal<Snapp> installedSnapps() {
		return _snapps.output();
	}

}
