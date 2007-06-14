package sneer.kernel.business.contacts;

import sneer.apps.messages.ChatEvent;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;

public interface Contact {

	Signal<String> nick();
	Signal<String> host();
	Signal<Integer> port();

	Signal<Boolean> isOnline();
}
