package sneer.apps.conversations.business.impl;

import java.awt.Rectangle;

import sneer.apps.conversations.business.AppPersistence;
import sneer.apps.conversations.business.AppPersistenceSource;
import sneer.apps.conversations.business.ConversationsPersistenceSource;
import sneer.apps.conversations.business.MessageInfo;
import sneer.kernel.business.contacts.ContactId;
import wheel.lang.Omnivore;
import wheel.lang.Pair;
import wheel.reactive.maps.MapSource;
import wheel.reactive.maps.impl.MapSourceImpl;

public class AppPersistenceSourceImpl implements AppPersistenceSource{

	private class MyOutput implements AppPersistence{

		@Override
		public ConversationsPersistenceSource persistenceFor(ContactId contactId) {
			ConversationsPersistenceSource source = _persistenceByContactId.output().currentGet(contactId);
			if (source != null)
				return source;
			creator().consume(contactId);
			return _persistenceByContactId.output().currentGet(contactId);
		}
		
	}
	
	public AppPersistence output(){
		return _output;
	}
	
	AppPersistence _output = new MyOutput();
	
	private final MapSource<ContactId,ConversationsPersistenceSource> _persistenceByContactId = new MapSourceImpl<ContactId,ConversationsPersistenceSource>();

	@Override
	public Omnivore<ContactId> creator() {
		return new Omnivore<ContactId>(){ @Override public void consume(ContactId contactId) {
			System.out.println("creating new Persistence for "+contactId);
			ConversationsPersistenceSource persistence = new ConversationsPersistenceSourceImpl();
			_persistenceByContactId.put(contactId, persistence);
		}};
	}
	
	//Refactor: I need to use delegate for setters because Bubble is not a recursive proxy... :(
	public Omnivore<Pair<ContactId,MessageInfo>> archive() {
		return new Omnivore<Pair<ContactId,MessageInfo>>(){ public void consume(Pair<ContactId,MessageInfo> pair) {
			_persistenceByContactId.output().currentGet(pair._a).archive().consume(pair._b);
		}};
	}

	//Refactor: I need to use delegate for setters because Bubble is not a recursive proxy... :(
	public Omnivore<Pair<ContactId,Rectangle>> boundsSetter() {
		return new Omnivore<Pair<ContactId,Rectangle>>(){ public void consume(Pair<ContactId,Rectangle> pair) {
			_persistenceByContactId.output().currentGet(pair._a).boundsSetter().consume(pair._b);
		}};
	}
		
}
