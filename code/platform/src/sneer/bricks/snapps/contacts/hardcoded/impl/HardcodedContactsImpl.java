package sneer.bricks.snapps.contacts.hardcoded.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.pulp.contacts.Contact;
import sneer.bricks.pulp.contacts.ContactManager;
import sneer.bricks.pulp.internetaddresskeeper.InternetAddressKeeper;
import sneer.bricks.pulp.keymanager.KeyManager;
import sneer.bricks.pulp.reactive.Signals;
import sneer.bricks.snapps.contacts.hardcoded.HardcodedContacts;
import sneer.bricks.snapps.contacts.stored.ContactStore;
import sneer.foundation.brickness.PublicKey;
import sneer.foundation.lang.Consumer;

public class HardcodedContactsImpl implements HardcodedContacts {

	private final ContactManager _contactManager = my(ContactManager.class);

	HardcodedContactsImpl(){
		my(Signals.class).receive(my(ContactStore.class).failToRestoreContacts(), 
			new Consumer<Boolean>(){ @Override public void consume(Boolean fail) {
				if(!fail) return;
				
				for (ContactInfo contact : contacts())
					add(contact);
			}
		});
	}
	
	private void add(ContactInfo contact) {
		String nick = contact._nick;
		addAddress(nick, contact._host, contact._port);
		
		for (String host : alternativeHostsFor(nick))
			addAddress(nick, host, contact._port);
	}

	private String[] alternativeHostsFor(String nick) {
		if (nick.equals("Kalecser")) return new String[]{"10.42.11.165"};
		if (nick.equals("Klaus")) return new String[]{"200.169.90.89", "10.42.11.19"};
		return new String[]{};
	}

	private ContactInfo[] contacts() {
		return new ContactInfo[] {
			new ContactInfo("Agnaldo4j", "agnaldo4j.selfip.com", 5923),
			new ContactInfo("Bamboo", "rodrigobamboo.dyndns.org", 5923),
			new ContactInfo("Bihaiko", "bihaiko.dyndns.org", 5923),
			new ContactInfo("Daniel Santos", "dfcsantos.homelinux.com", 7777),
			new ContactInfo("Douglas Giacomini", "dtgiacomini.dyndns.org", 5923),
			new ContactInfo("Dummy", "localhost", 7777),
			new ContactInfo("Duno", "duno.dyndns.org", 5923),
			new ContactInfo("Igor Arouca", "igorarouca.dyndns.org", 6789),
			new ContactInfo("Kalecser", "kalecser.dyndns.org", 7770),
			new ContactInfo("Klaus", "klausw.selfip.net", 5923),
			new ContactInfo("Nell", "anelisedaux.dyndns.org", 5924),
			new ContactInfo("Priscila Vriesman", "priscilavriesman.dyndns.org", 7770),
			new ContactInfo("Ramon Tramontini", "ramontramontini.dyndns.org", 7770),
			new ContactInfo("Sneer Team", "sovereigncomputing.net", 7711),
			new ContactInfo("Vitor Pamplona", "vfpamp.dyndns.org", 5923),
		};
	}

	static class ContactInfo {
		final String _nick;
		final String _host;
		final int _port;

		ContactInfo(String nick, String host, int port) {
			_nick = nick;
			_host = host;
			_port = port;
		}
	}

	private void addAddress(String nick, String host, int port) {
		Contact contact = produceContact(nick);
		my(InternetAddressKeeper.class).add(contact, host, port);
	}

	@SuppressWarnings("deprecation")
	private PublicKey mickeyMouseKey(String nick) {
		return keyManager().generateMickeyMouseKey(nick);
	}

	private Contact produceContact(String nick) {
		Contact result = _contactManager.contactGiven(nick);
		if (result != null) return result;
		
		result = _contactManager.produceContact(nick);
		keyManager().addKey(result, mickeyMouseKey(nick));
		return result;
	}

	private KeyManager keyManager() {
		return my(KeyManager.class);
	}
}