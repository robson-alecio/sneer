package sneer.kernel.business;

import wheel.lang.Consumer;

public interface ContactSource {

	Contact output();

	Consumer<String> nickSetter();
	Consumer<String> hostSetter();
	Consumer<Integer> portSetter();
	Consumer<Boolean> isOnlineSetter();

}