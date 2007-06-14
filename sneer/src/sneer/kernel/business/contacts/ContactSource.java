package sneer.kernel.business.contacts;

import sneer.apps.messages.ChatEvent;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;

public interface ContactSource {

	Contact output();

	Omnivore<String> nickSetter();
	Omnivore<String> hostSetter();
	Consumer<Integer> portSetter();
	Omnivore<Boolean> isOnlineSetter();

}