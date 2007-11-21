package sneer.apps.conversations.business;

import wheel.reactive.Signal;

public interface Message {
	Signal<String> author();
	Signal<String> message();
	Signal<Long> timestamp();
}
