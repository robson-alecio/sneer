package sneer.bricks.snapps.contacts.hardcoded.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.network.social.Contact;
import sneer.bricks.network.social.ContactManager;
import sneer.bricks.pulp.internetaddresskeeper.InternetAddressKeeper;
import sneer.bricks.snapps.contacts.hardcoded.HardcodedContacts;

public class HardcodedContactsImpl implements HardcodedContacts {

	private final ContactManager _contactManager = my(ContactManager.class);

	HardcodedContactsImpl(){
		if(_contactManager.contacts().currentElements().size()>0) 
			return;
				
		for (ContactInfo contact : contacts())
			add(contact);
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
			new ContactInfo("Bihaiko", "bihaiko.dyndns.org", 6789),
			new ContactInfo("Célio", "ccidral.dyndns.org", 9789),
			new ContactInfo("Daniel Santos", "dfcsantos.homelinux.com", 7777),
			new ContactInfo("Douglas Giacomini", "dtgiacomini.dyndns.org", 5923),
			new ContactInfo("Dummy", "localhost", 7777),
			new ContactInfo("Igor Arouca", "igorarouca.dyndns.org", 6789),
			new ContactInfo("Kalecser", "kalecser.dyndns.org", 7770),
			new ContactInfo("Klaus", "klausw.selfip.net", 5923),
			new ContactInfo("Nell", "anelisedaux.dyndns.org", 5924),
			new ContactInfo("Priscila Vriesman", "priscilavriesman.dyndns.org", 7770),
			new ContactInfo("Ramon Tramontini", "ramontramontini.dyndns.org", 7770),
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
		Contact contact = _contactManager.produceContact(nick);
		my(InternetAddressKeeper.class).add(contact, host, port);
	}

}