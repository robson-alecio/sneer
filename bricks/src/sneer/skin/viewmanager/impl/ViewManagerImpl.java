package sneer.skin.viewmanager.impl;

import sneer.skin.viewmanager.Snapp;
import sneer.skin.viewmanager.ViewManager;

class ViewManagerImpl implements ViewManager {

	private Snapp _onlyOnePartyViewForNow;

	@Override
	public void register(Snapp view) {
		_onlyOnePartyViewForNow = view;
	}

	@Override
	public Snapp getOnlyOneSnappForNow() {
		return _onlyOnePartyViewForNow;
	}

}
