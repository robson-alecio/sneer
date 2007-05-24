package sneer.kernel.business;

import wheel.lang.Consumer;

public interface ContactSource extends Contact { //Fix: Should not extend Contact. Only output() is a Contact.

	Contact output();

	Consumer<String> nickSetter();
	Consumer<String> hostSetter();
	Consumer<Integer> portSetter();
	Consumer<Boolean> isOnlineSetter();

}