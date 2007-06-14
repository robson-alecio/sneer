package sneer.kernel.communication;

import sneer.apps.conversations.Message;
import wheel.lang.Consumer;
import wheel.reactive.Signal;

public interface Connection<T> {

	Signal<T> input();
	Consumer<T> output();

}
