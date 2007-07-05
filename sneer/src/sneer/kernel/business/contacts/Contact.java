package sneer.kernel.business.contacts;

import wheel.reactive.Signal;

public interface Contact {
	public static final String CONFIRMED_STATE = "confirmed"; //everything is ok
	public static final String UNCONFIRMED_STATE = "unconfirmed"; //needs confirmation it is the right guy
	public static final String LOCATION_ERROR_STATE = "location error"; //ip/dns does not exist
	public static final String ERROR_STATE = "error"; //any other error (generic errors are promoted and become state constants as needed)
	
	Signal<String> nick();
	Signal<String> host();
	Signal<Integer> port();
	Signal<String> state();

	Signal<String> publicKey();

	Signal<Boolean> isOnline();

	ContactId id();
}
