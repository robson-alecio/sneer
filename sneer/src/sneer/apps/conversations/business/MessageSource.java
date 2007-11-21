package sneer.apps.conversations.business;

import wheel.lang.Omnivore;

public interface MessageSource {

	Message output();
	
	Omnivore<String> authorSetter();
	Omnivore<String> messageSetter();
	Omnivore<Long> timestampSetter();
	
}
