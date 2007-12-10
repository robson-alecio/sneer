package sneer.kernel.business.contacts.impl;

import static wheel.i18n.Language.translate;
import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.business.contacts.ContactAttributesSource;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.business.contacts.ContactInfo;
import sneer.kernel.business.contacts.ContactManager;
import sneer.kernel.business.contacts.ContactPublicKeyInfo;
import wheel.lang.Consumer;
import wheel.lang.Counter;
import wheel.lang.Omnivore;
import wheel.lang.Pair;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.lists.ListSignal;
import wheel.reactive.lists.ListSource;
import wheel.reactive.lists.impl.ListSourceImpl;

public class ContactManagerImpl implements ContactManager {
	
	private final ListSource<ContactAttributesSource> _contactSources = new ListSourceImpl<ContactAttributesSource>();
	private final ListSource<ContactAttributes> _contacts = new ListSourceImpl<ContactAttributes>(); 	//Refactor: use a reactive "ListCollector" instead of keeping this redundant list.
	private final Counter _contactIdSource = new Counter();

	
	@Override
	public Consumer<ContactInfo> contactAdder() {
		return new Consumer<ContactInfo>() { @Override public void consume(ContactInfo info) throws IllegalParameter {
			checkDuplicateNick(info._nick);

			ContactAttributesSource contact = new ContactAttributesSourceImpl(info._nick, info._host, info._port, info._publicKey, _contactIdSource.next());
			_contactSources.add(contact);
			_contacts.add(contact.output());
		}};
	}

	@Override
	public Omnivore<ContactPublicKeyInfo> contactPublicKeyUpdater() {
		return new ContactPublicKeyUpdater(_contactSources);
	}

	@Override
	public Consumer<Pair<ContactId, String>> contactNickChanger() {
		return new Consumer<Pair<ContactId,String>>() { @Override public void consume(Pair<ContactId, String> nickChange) throws IllegalParameter {
			ContactId contactId = nickChange._a;
			String newNick = nickChange._b;
			checkDuplicateNick(newNick);
			findContactSource(contactId).nickSetter().consume(newNick);	
		}};

	}

	@Override
	public Omnivore<Pair<ContactId, String>> contactMsnAddressChanger() {
		return new Omnivore<Pair<ContactId,String>>() { @Override public void consume(Pair<ContactId, String> addressChange) {
			ContactId contactId = addressChange._a;
			String newMsnAddress = addressChange._b;
			findContactSource(contactId).msnAddressSetter().consume(newMsnAddress);	
		}};

	}

	@Override
	public Omnivore<ContactId> contactRemover() {
		return new Omnivore<ContactId>() { @Override public void consume(ContactId contactId) {
			ContactAttributesSource contactSource = findContactSource(contactId);
			_contactSources.remove(contactSource);
			_contacts.remove(contactSource.output());
		}};
	}

	private ContactAttributesSource findContactSource(ContactId contactId) {
		for (ContactAttributesSource candidate : _contactSources.output())
			if (candidate.output().id().equals(contactId))
				return candidate;
		
		throw new IllegalArgumentException("contactId not found");
	}

	private void checkDuplicateNick(String newNick) throws IllegalParameter {
		if (findContactSource(newNick) != null)
			throw new IllegalParameter(translate("There already is a contact with nickname: %1$s", newNick));
	};

	private ContactAttributesSource findContactSource(String nick) {
		for (ContactAttributesSource candidate:_contactSources.output()) // Optimize
			if (candidate.output().nick().currentValue().equals(nick))
				return candidate;

		return null;
	}

	public ListSignal<ContactAttributes> output() {
		return _contacts.output();
	}

}
