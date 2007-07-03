package sneer.kernel.business.contacts.impl;

import sneer.kernel.business.contacts.ContactPublicKeyInfo;
import sneer.kernel.business.contacts.ContactSource;
import wheel.lang.Omnivore;
import wheel.lang.exceptions.IllegalParameter;
import wheel.reactive.lists.ListSource;

public class ContactPublicKeyUpdater implements Omnivore<ContactPublicKeyInfo> {

	private final ListSource<ContactSource> _contactSources;

	public ContactPublicKeyUpdater(ListSource<ContactSource> contactSources) {
		_contactSources = contactSources;
	}

	@Override
	public void consume(ContactPublicKeyInfo info) {
		ContactSource contact = findContactByNick(info._nick);
		if (contact == null) throw new IllegalStateException();
		
		if (findContactByPublicKey(info._publicKey) != null)
			throw new IllegalArgumentException("Some contact already had that public key.");
		
		contact.publicKeySetter().consume(info._publicKey);
	}

	private ContactSource findContactByNick(String nick) {
		for (ContactSource contactSource : _contactSources.output()) { // Optimize
			String candidateNick = contactSource.output().nick().currentValue();
			if (candidateNick.equals(nick)) return contactSource;
		}
		return null;
	}

	private ContactSource findContactByPublicKey(String pk) {
		for (ContactSource contactSource : _contactSources.output()) { // Optimize
			String candidatePK = contactSource.output().publicKey().currentValue();
			if (candidatePK.equals(pk)) return contactSource;
		}
		return null;
	}

}
