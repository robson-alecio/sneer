package addressbook.business;

import java.awt.Rectangle;

import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.lang.Pair;


public interface AddressBookSource {

	AddressBook output();
	
	Omnivore<Rectangle> boundsSetter();
	
	Consumer<PersonInfo> personAdder();
	Omnivore<PersonInfo> personRemover();
	Consumer<Pair<PersonInfo, PersonInfo>> personUpdater();
}
