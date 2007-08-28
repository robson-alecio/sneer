package sneer.kernel.pointofview.impl;

import sneer.kernel.business.Business;
import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Operator;
import sneer.kernel.communication.Packet;
import sneer.kernel.pointofview.Contact;
import sneer.kernel.pointofview.Party;
import sneer.kernel.pointofview.PointOfViewPacket;
import wheel.graphics.JpgImage;
import wheel.lang.Functor;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.impl.SourceImpl;
import wheel.reactive.lists.Collector;
import wheel.reactive.lists.ListSignal;

public class Me implements Party {

	public Me(Business business, Operator operator, Channel channel) {
		_business = business;
		_operator = operator;
		_channel = channel;
		
		_contacts = createContactsListSignal();
		
		_business.ownName().addReceiver(stringChangedBroadcaster());
		_business.thoughtOfTheDay().addReceiver(stringChangedBroadcaster());
		_business.picture().addReceiver(pictureChangedBroadcaster());
		_business.profile().addReceiver(stringChangedBroadcaster());
	}
	

	private final Business _business;
	private final ListSignal<Contact> _contacts;
	private final Operator _operator;
	private final Channel _channel;

	
	private Omnivore<String> stringChangedBroadcaster() { //this will change to individual changes in the future
		return new Omnivore<String>() { @Override public void consume(String newName) {
			for (Contact contact : _contacts)
				sendChanges(contact);
		}};
	}
	
	private Omnivore<JpgImage> pictureChangedBroadcaster() {
		return new Omnivore<JpgImage>() { @Override public void consume(JpgImage newPicture) {
			for (Contact contact : _contacts)
				sendChanges(contact);
		}};
	}
		
	private void sendChanges(Contact contact) {
		_channel.output().consume(new Packet(contact.id(), new PointOfViewPacket(_business.ownName().currentValue(),_business.thoughtOfTheDay().currentValue(),_business.picture().currentValue(),_business.profile().currentValue())));
	};

	private ListSignal<Contact> createContactsListSignal() {
		return new Collector<ContactAttributes, Contact>(_business.contactAttributes(), contactCreator()).output();
	}
	
	private Functor<ContactAttributes, Contact> contactCreator() {
		return new Functor<ContactAttributes, Contact>() { @Override 	public Contact evaluate(ContactAttributes attributes) {
			return new ImmediateContact(attributes, _operator.connectMeWith(attributes.id()).isOnline(), _channel);
		}};
	}


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
		return new SourceImpl<Boolean>(true).output(); //Fix: is this correct?
	}

	@Override
	public Signal<Boolean> isOnline() {
		return new SourceImpl<Boolean>(true).output(); //Fix: is this correct?
	}

	@Override
	public Signal<String> host() {
		return new SourceImpl<String>("localhost").output(); //Implement Make this a list of possible host:port addresses.
	}

	@Override
	public Signal<Integer> port() {
		return _business.sneerPort();
	}

	@Override
	public String toString(){
    	return _business.ownName().currentValue();
    }

	@Override
	public Contact currentContact(String nick) {
		for (Contact candidate : _contacts)
			if (candidate.nick().currentValue().equals(nick)) return candidate;
		
		return null;
	}

	@Override
	public Signal<String> publicKey() {
		return _business.publicKey();
	}

	public Signal<JpgImage> picture() {
		return _business.picture();
	}

	public Signal<String> profile() {
		return _business.profile();
	}

	public Signal<String> thoughtOfTheDay() {
		return _business.thoughtOfTheDay();
	}

}
