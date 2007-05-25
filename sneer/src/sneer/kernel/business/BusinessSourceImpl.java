package sneer.kernel.business;

import java.io.Serializable;

import wheel.lang.Consumer;
import wheel.lang.IntegerConsumerBoundaries;
import wheel.lang.Omnivore;
import wheel.lang.exceptions.NotImplementedYet;
import wheel.reactive.Signal;
import wheel.reactive.SourceImpl;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListSource;
import wheel.reactive.lists.impl.ListSourceImpl;


public class BusinessSourceImpl implements BusinessSource, Business, Serializable { //Refactor: Create a separate class for BusinessImpl.

	private SourceImpl<String> _ownName = new SourceImpl<String>("");

	private SourceImpl<Integer> _sneerPortNumber = new SourceImpl<Integer>(0);

	private final ListSource<ContactSource> _contactSources = new ListSourceImpl<ContactSource>();
	private final ListSource<Contact> _contacts = new ListSourceImpl<Contact>(); 	//Refactor: use a reactive "ListCollector" instead of keeping this redundant list.


	
	public Signal<String> ownName() {
		return _ownName.output();
	}
	
	public Omnivore<String> ownNameSetter() {
		return _ownName.setter();
	}

	public Signal<Integer> sneerPort() {
		return _sneerPortNumber.output();
	}
	
	public Consumer<Integer> sneerPortSetter() {
		return new IntegerConsumerBoundaries("Sneer Port", _sneerPortNumber.setter(), 0, 65535);
	}

	public ListSignal<Contact> contacts() {
		return _contacts.output();
	}



	private static final long serialVersionUID = 1L;


	public void removeContact(Contact contact) {	//Implement: do this with a Consumer<String> for the nick.
		throw new NotImplementedYet();
	}

	public Consumer<ContactInfo> contactAdder() {
		return new ContactAdder(_contactSources, _contacts);
	}

	public Business output() {
		return this;
	}
}
