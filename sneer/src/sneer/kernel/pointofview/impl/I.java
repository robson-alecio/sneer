package sneer.kernel.pointofview.impl;

import sneer.kernel.business.Business;
import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.communication.Operator;
import sneer.kernel.pointofview.Contact;
import sneer.kernel.pointofview.Party;
import wheel.lang.Functor;
import wheel.reactive.Signal;
import wheel.reactive.impl.ConstantSignal;
import wheel.reactive.lists.Collector;
import wheel.reactive.lists.ListSignal;

public class I implements Party {

	public I(Business business, Operator operator) {
		_business = business;
		_operator = operator;
		_contacts = createContactsListSignal();
	}

	private ListSignal<Contact> createContactsListSignal() {
		return new Collector<ContactAttributes, Contact>(_business.contactAttributes(), contactCreator()).output();
	}
	
	private Functor<ContactAttributes, Contact> contactCreator() {
		return new Functor<ContactAttributes, Contact>() { @Override 	public Contact evaluate(ContactAttributes attributes) {
			return new ImmediateContact(attributes, _operator.connectMeWith(attributes.id()).isOnline());
		}};
	}


	private final Business _business;
	private final ListSignal<Contact> _contacts;
	private final Operator _operator;

	@Override
	public ListSignal<Contact> contacts() {
		return _contacts;
	}

	@Override
	public Signal<String> name() {
		return _business.ownName();
	}

	@Override
	public Signal<Boolean> publicKeyConfirmed() {
		return new ConstantSignal<Boolean>(true); //Fix: is this correct?
	}

	@Override
	public Signal<Boolean> isOnline() {
		return new ConstantSignal<Boolean>(true); //Fix: is this correct?
	}

	@Override
	public Signal<String> host() {
		return new ConstantSignal<String>("localhost"); //Implement Make this a list of possible host:port addresses.
	}

	@Override
	public Signal<Integer> port() {
		return _business.sneerPort();
	}

	@Override
	public String toString(){
    	return _business.ownName().currentValue();
    }

}
