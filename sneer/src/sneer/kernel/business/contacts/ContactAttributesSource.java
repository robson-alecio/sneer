package sneer.kernel.business.contacts;

import sneer.kernel.business.contacts.impl.ContactPublicKeyUpdater;
import wheel.lang.Consumer;
import wheel.lang.Omnivore;

public interface ContactAttributesSource {

	ContactAttributes output();

	Omnivore<String> nickSetter();
	Omnivore<String> hostSetter();
	Consumer<Integer> portSetter();
	Omnivore<String> stateSetter();
	Omnivore<Boolean> isOnlineSetter();
	Omnivore<String> publicKeySetter();

}