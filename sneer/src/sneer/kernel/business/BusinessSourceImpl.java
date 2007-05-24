package sneer.kernel.business;

import java.io.Serializable;

import wheel.lang.Consumer;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.SourceImpl;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListSource;
import wheel.reactive.lists.impl.ListSourceImpl;


public class BusinessSourceImpl implements BusinessSource, Business, Serializable { //Refactor: Create a separate class for BusinessImpl.

	private SourceImpl<String> _ownName = new SourceImpl<String>("");

	private SourceImpl<Integer> _sneerPortNumber = new SourceImpl<Integer>(0);

	private final ListSource<Contact> _contacts = new ListSourceImpl<Contact>();

	
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
		return _sneerPortNumber.setter();
	}

	public ListSignal<Contact> contacts() {
		return _contacts.output();
	}



	private static final long serialVersionUID = 1L;


	public void removeContact(Contact contact) {
		
		if (!_contacts.remove(contact))
			throw new IllegalArgumentException("Impossible to remove contact");
		
	}

	public Consumer<ContactInfo> contactAdder() {
		return new ContactAdder(_contacts);
	}

	public Business output() {
		return this;
	}
}
