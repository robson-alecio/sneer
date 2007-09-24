package sneer.kernel.pointofview;

import java.io.Serializable;

import sneer.kernel.business.contacts.ContactId;
import wheel.reactive.Signal;

public interface Contact extends Serializable{

	Signal<String> nick();
	Party party();
	
	ContactId id();
	
}
