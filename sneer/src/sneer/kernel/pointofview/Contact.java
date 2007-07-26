package sneer.kernel.pointofview;

import sneer.kernel.business.contacts.ContactId;
import wheel.reactive.Signal;

public interface Contact {

	Signal<String> nick();
	Party party();
	
	ContactId id();
	
}
