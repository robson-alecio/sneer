package snapps.contacts.hardcoded.impl;

import static sneer.commons.environments.Environments.my;
import snapps.contacts.hardcoded.HardcodedContacts;
import sneer.brickness.PublicKey;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.pulp.internetaddresskeeper.InternetAddressKeeper;
import sneer.pulp.keymanager.KeyManager;

public class HardcodedContactsImpl implements HardcodedContacts {

	{
		for (ContactInfo contact : contacts())
		addConnections(contact);
	}
	
	private void addConnections(ContactInfo contact) {
		String nick = contact._nick;
		addConnectionTo(nick, contact._host, contact._port);
		
		for (String host : alternativeHostsFor(nick))
			addConnectionTo(nick, host, contact._port);
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
			new ContactInfo("Kalecser", "kalecser.dyndns.org", 7770),
			new ContactInfo("Klaus", "klausw.selfip.net", 5923),
			new ContactInfo("Nell", "anelisedaux.dyndns.org", 5924),
			new ContactInfo("Priscila Vriesman", "priscilavriesman.dyndns.org", 7770),
			new ContactInfo("Ramon Tramontini", "ramontramontini.dyndns.org", 7770),
			new ContactInfo("Sneer Team, The", "sovereigncomputing.net", 7711),
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

	private void addConnectionTo(String nick, String host, int port) {
		Contact contact = produceContact(nick);
		my(InternetAddressKeeper.class).add(contact, host, port);
	}

	@SuppressWarnings("deprecation")
	private PublicKey mickeyMouseKey(String nick) {
		return my(KeyManager.class).generateMickeyMouseKey(nick);
	}

	private Contact produceContact(String nick) {
		Contact result = my(ContactManager.class).contactGiven(nick);
		if (result != null) return result;
		
		result = my(ContactManager.class).addContact(nick);
		my(KeyManager.class).addKey(result, mickeyMouseKey(nick));
		return result;
	}

	

}