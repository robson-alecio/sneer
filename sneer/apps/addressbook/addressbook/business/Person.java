package addressbook.business;

import wheel.reactive.Signal;

public interface Person extends Comparable<Person>{

	Signal<String> name();
	Signal<String> address();
	Signal<String> phone();
	Signal<String> email();
	
}
