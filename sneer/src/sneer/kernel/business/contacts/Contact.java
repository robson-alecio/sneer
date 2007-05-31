package sneer.kernel.business.contacts;

import sneer.kernel.business.chat.ChatEvent;
import sneer.kernel.gui.contacts.ContactListPrinter;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;

public interface Contact {

	Signal<String> nick();
	Signal<String> host();
	Signal<Integer> port();

	Signal<Boolean> isOnline();
	
	ListSignal<ChatEvent> chatEventsPending();
}
