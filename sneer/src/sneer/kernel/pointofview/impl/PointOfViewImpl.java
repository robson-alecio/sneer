package sneer.kernel.pointofview.impl;

import sneer.kernel.business.Business;
import sneer.kernel.pointofview.Contact;
import sneer.kernel.pointofview.Party;
import sneer.kernel.pointofview.PointOfView;
import wheel.reactive.lists.ListSignal;

public class PointOfViewImpl implements PointOfView {

	public PointOfViewImpl(Business business, ListSignal<Contact> contacts) {
		_me = new Me(business, contacts);
	}
	
	private final Party _me;

	public Party me() {
		return _me;
	}
	
}
