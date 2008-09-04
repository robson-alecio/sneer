package main.impl;

import snapps.contacts.ContactsSnapp;
import snapps.owner.OwnerSnapp;
import sneer.kernel.container.Inject;
import sneer.pulp.connection.SocketOriginator;
import sneer.pulp.connection.SocketReceiver;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.pulp.internetaddresskeeper.InternetAddressKeeper;
import sneer.pulp.port.PortKeeper;
import sneer.skin.dashboard.Dashboard;

class MainDemoBrickImpl {

	@Inject	@SuppressWarnings("unused")
	private static Dashboard _gui1;
	
	@Inject	@SuppressWarnings("unused")
	private static OwnerSnapp _gui2;
	
	@Inject	@SuppressWarnings("unused")
	private static ContactsSnapp _gui3;

	
	@Inject	@SuppressWarnings("unused")
	private static SocketOriginator _networkDaemon1;
	
	@Inject	@SuppressWarnings("unused")
	private static SocketReceiver _networkDaemon2;

	
	@Inject
	private static PortKeeper _portKeeper;

	@Inject
	private static ContactManager _contactManager;
	
	@Inject
	private static InternetAddressKeeper _addressKeeper;

	
	MainDemoBrickImpl() {
		_portKeeper.portSetter().consume(7777);
		
		addConnectionTo("Sandro", "localhost");
		addConnectionTo("Klaus", "klaus.selfip.net", 5923);
		addConnectionTo("Klaus", "200.169.90.89", 5923);
		addConnectionTo("Bamboo", "somehost");
	}
	

	private void addConnectionTo(String nick, String host) {
		addConnectionTo(nick, host, 7777);
	}
	
	
	private void addConnectionTo(String nick, String host, int port) {
		Contact contact = produceContact(nick);
		_addressKeeper.add(contact, host, port);
	}


	private Contact produceContact(String nick) {
		Contact contact = _contactManager.contactGiven(nick);
		if (contact != null) return contact;
		
		return _contactManager.addContact(nick);
	}

	
}
