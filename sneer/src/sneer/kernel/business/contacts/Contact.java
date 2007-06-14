package sneer.kernel.business.contacts;

import wheel.reactive.Signal;

public interface Contact {

	Signal<String> nick();
	Signal<String> host();
	Signal<Integer> port();

	Signal<Boolean> isOnline();
}
