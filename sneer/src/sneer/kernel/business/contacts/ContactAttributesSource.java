package sneer.kernel.business.contacts;

import wheel.lang.Consumer;
import wheel.lang.Omnivore;

public interface ContactAttributesSource {

	ContactAttributes output();

	Omnivore<String> nickSetter();
	Omnivore<String> hostSetter();
	Consumer<Integer> portSetter();
	Omnivore<String> publicKeySetter();

}