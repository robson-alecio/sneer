package addressbook.business;

import java.awt.Rectangle;

import wheel.reactive.Signal;
import wheel.reactive.lists.ListSignal;

public interface AddressBook {
	
	Signal<Rectangle> bounds();
	
	ListSignal<Person> personList();
	
}
