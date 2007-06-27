package sneer.kernel.business.impl;

import sneer.kernel.business.Business;
import sneer.kernel.business.BusinessSource;
import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.ContactInfo;
import sneer.kernel.business.contacts.ContactSource;
import sneer.kernel.business.contacts.OnlineEvent;
import sneer.kernel.business.contacts.impl.ContactAdder;
import wheel.io.network.PortNumberSource;
import wheel.lang.Consumer;
import wheel.lang.Counter;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.SourceImpl;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListSource;
import wheel.reactive.lists.impl.ListSourceImpl;


public class BusinessSourceImpl implements BusinessSource  { //Refactor: Create a separate class for BusinessImpl.


	private final class MyOutput implements Business {

		@Override
		public ListSignal<Contact> contacts() {
			return _contacts.output();
		}

		@Override
		public Signal<String> ownName() {
			return _ownName.output();
		}

		@Override
		public Signal<Integer> sneerPort() {
			return _sneerPortNumber.output();
		}

		@Override
		public Signal<String> publicKey() {
			return _publicKey.output();
		}

	}

	private Source<String> _ownName = new SourceImpl<String>("");
	private final Source<String> _publicKey = new SourceImpl<String>("");

	private final PortNumberSource _sneerPortNumber = new PortNumberSource(0);

	private final ListSource<ContactSource> _contactSources = new ListSourceImpl<ContactSource>();
	private final ListSource<Contact> _contacts = new ListSourceImpl<Contact>(); 	//Refactor: use a reactive "ListCollector" instead of keeping this redundant list.
	private final Counter _contactIdSource = new Counter();
	
	private final Business _output = new MyOutput();



	@Override
	public Omnivore<String> ownNameSetter() {
		return  _ownName.setter();
	}
	
	@Override
	public Consumer<Integer> sneerPortSetter() {
		return _sneerPortNumber.setter();
	}

	@Override
	public Consumer<ContactInfo> contactAdder() {
		return new ContactAdder(_contactSources, _contacts, _contactIdSource);
	}

	@Override
	public Business output() {
		return _output;
	}

	@Override
	public Omnivore<OnlineEvent> contactOnlineSetter() {
		return new Omnivore<OnlineEvent>(){
			@Override
			public void consume(OnlineEvent onlineEvent) {
				ContactSource contactSource = findContactSource(onlineEvent._nick);
				if (contactSource != null) setIsOnline(contactSource, onlineEvent._isOnline);
			}
		};
	}
	
	private void setIsOnline(ContactSource contactSource, boolean isOnline) {
		Boolean wasOnline = contactSource.output().isOnline().currentValue();
		if (isOnline == wasOnline) return;
		contactSource.isOnlineSetter().consume(isOnline);
	}

	private ContactSource findContactSource(String nick) {
		for (ContactSource candidate:_contactSources) { // Optimize
			if (candidate.output().nick().currentValue().equals(nick))
				return candidate;
		}
		return null;
	}

	public Omnivore<String> publicKeySetter() {
		return _publicKey.setter();
	}

}
