package sneer.skin.viewmanager.impl;

import sneer.skin.viewmanager.PartyView;
import sneer.skin.viewmanager.ViewManager;

class ViewManagerImpl implements ViewManager {

	private PartyView _onlyOnePartyViewForNow;

	@Override
	public void register(PartyView view) {
		_onlyOnePartyViewForNow = view;
	}

	@Override
	public PartyView getOnlyOnePartyViewForNow() {
		return _onlyOnePartyViewForNow;
	}

}
