package sneer.pulp.contacts;

import sneer.pulp.reactive.Signal;
import wheel.lang.PickyConsumer;

public interface Contact {
	
	Signal<String> nickname();
	PickyConsumer<String> nicknameSetter();
	
}
