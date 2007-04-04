package sneer.kernel.business;

import sneer.kernel.business.essence.Contact;
import sneer.kernel.business.essence.Essence;

public class Business {

	public Business(Essence essence) {
		_essence = essence;
	}
	
	private final Essence _essence;

	public Essence essence() {
		return _essence;
	}

	public boolean isOnline(Contact contact) {
		return false;
	}

}
