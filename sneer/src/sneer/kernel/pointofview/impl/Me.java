package sneer.kernel.pointofview.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.kernel.business.Business;
import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Operator;
import sneer.kernel.communication.Packet;
import sneer.kernel.pointofview.Contact;
import sneer.kernel.pointofview.Party;
import sneer.kernel.pointofview.PointOfViewPacket;
import wheel.graphics.JpgImage;
import wheel.lang.Functor;
import wheel.lang.Omnivore;
import wheel.lang.Pair;
import wheel.reactive.Signal;
import wheel.reactive.impl.SourceImpl;
import wheel.reactive.lists.Collector;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.impl.VisitingListReceiver;

public class Me implements Party {

	public Me(Business business, Operator operator, Channel channel, Signal<Boolean> isOnlineOnMsn, Signal<Pair<String, Boolean>> isContactOnlineOnMsnEvents) {
		_business = business;
		_operator = operator;
		_channel = channel;
		_isOnlineOnMsn = isOnlineOnMsn;
		_isContactOnlineOnMsnEvents = isContactOnlineOnMsnEvents;

		_contacts = createContactsListSignal();
		
		_business.ownName().addReceiver(stringChangedBroadcaster());
		_business.thoughtOfTheDay().addReceiver(stringChangedBroadcaster());
		_business.picture().addReceiver(pictureChangedBroadcaster());
		_business.profile().addReceiver(stringChangedBroadcaster());
		
		
		for (Contact contact : _contacts) 
			contact.party().isOnline().addReceiver(sendCurrentStatus(contact)); 
		_contacts.addListReceiver(new ListChangeReceiver());
	}
	
	private class ListChangeReceiver extends VisitingListReceiver {

		@Override
		public void elementAdded(int index) {
			addReceiverToElement(index); 
		}

		@Override
		public void elementToBeRemoved(int index) {
			removeReceiverFromElement(index);
		}

		@Override
		public void elementRemoved(int index) {
		}

		@Override
		public void elementToBeReplaced(int index) {
			removeReceiverFromElement(index);
		}

		@Override
		public void elementReplaced(int index) {
			addReceiverToElement(index); 
		}

	}
	
	private Map<ContactId, Omnivore<Boolean>> _statusReceiverByContactId = new HashMap<ContactId, Omnivore<Boolean>>();

	private void addReceiverToElement(int index) {
		Contact contact = _contacts.currentGet(index);
		Omnivore<Boolean> statusReceiver = sendCurrentStatus(contact);
		_statusReceiverByContactId.put(contact.id(), statusReceiver);
		contact.party().isOnline().addReceiver(statusReceiver);
	}

	private void removeReceiverFromElement(int index) {
		Contact contact = _contacts.currentGet(index);
		contact.party().isOnline().removeReceiver(_statusReceiverByContactId.remove(contact.id()));
	}
	
	private Omnivore<Boolean> sendCurrentStatus(final Contact contact) {
		return new Omnivore<Boolean>(){ public void consume(Boolean value) {
				if (value)
					sendChanges(contact);
		}};
	}

	private final Business _business;
	private final ListSignal<Contact> _contacts;
	private final Operator _operator;
	private final Channel _channel;
	private final Signal<Boolean> _isOnlineOnMsn;
	private final Signal<Pair<String, Boolean>> _isContactOnlineOnMsnEvents;

	
	private Omnivore<String> stringChangedBroadcaster() { //this will change to individual changes in the future
		return new Omnivore<String>() { @Override public void consume(String newName) {
			for (Contact contact : _contacts)
				sendChanges(contact);
		}};
	}
	
	//Refactor: Use TransferQueue instead.... callback will update source.
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
			return new ImmediateContact(attributes, _operator.connectMeWith(attributes.id()).isOnline(), _channel, _isContactOnlineOnMsnEvents);
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

	@Override
	public Signal<String> msnAddress() {
		return _business.msnAddress();
	}

	@Override
	public Signal<Boolean> isOnlineOnMsn() {
		return _isOnlineOnMsn;
	}

}
