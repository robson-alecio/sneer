package sneer.kernel.business.contacts.impl;

import sneer.kernel.business.contacts.ContactPublicKeyInfo;
import sneer.kernel.business.contacts.ContactAttributesSource;
import wheel.lang.Omnivore;
import wheel.reactive.lists.ListSource;
import static wheel.i18n.Language.*;

public class ContactPublicKeyUpdater implements Omnivore<ContactPublicKeyInfo> {

	private final ListSource<ContactAttributesSource> _contactSources;

	public ContactPublicKeyUpdater(ListSource<ContactAttributesSource> contactSources) {
		_contactSources = contactSources;
	}

	@Override
	public void consume(ContactPublicKeyInfo info) {
		ContactAttributesSource contact = findContactByNick(info._nick);
		if (contact == null) throw new IllegalStateException();
		
		if (findContactByPublicKey(info._publicKey) != null)
			throw new IllegalArgumentException(translate("Some contact already had that public key."));		
		contact.publicKeySetter().consume(info._publicKey);
	}

	private ContactAttributesSource findContactByNick(String nick) {
		for (ContactAttributesSource contactSource : _contactSources.output()) { // Optimize
			String candidateNick = contactSource.output().nick().currentValue();
			if (candidateNick.equals(nick)) return contactSource;
		}
		return null;
	}

	private ContactAttributesSource findContactByPublicKey(String pk) {
		for (ContactAttributesSource contactSource : _contactSources.output()) { // Optimize
			String candidatePK = contactSource.output().publicKey().currentValue();
			if (candidatePK.equals(pk)) return contactSource;
		}
		return null;
	}

}
