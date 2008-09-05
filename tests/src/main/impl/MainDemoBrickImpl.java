package main.impl;

import main.MainDemoBrick;
import snapps.contacts.ContactsSnapp;
import snapps.owner.OwnerSnapp;
import sneer.kernel.container.Inject;
import sneer.pulp.connection.SocketOriginator;
import sneer.pulp.connection.SocketReceiver;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.pulp.internetaddresskeeper.InternetAddressKeeper;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.own.name.OwnNameKeeper;
import sneer.pulp.port.PortKeeper;
import sneer.skin.dashboard.Dashboard;

class MainDemoBrickImpl implements MainDemoBrick {

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
	private static OwnNameKeeper _ownName;

	@Inject
	private static PortKeeper _portKeeper;

	@Inject
	private static ContactManager _contactManager;

	@Inject
	private static KeyManager _keyManager;
	
	@Inject
	private static InternetAddressKeeper _addressKeeper;



	@Override
	public void start(String ownName, int port) {
		System.out.println("Starting Sneer on port " + port);

		_ownName.nameSetter().consume(ownName);
		
		_portKeeper.portSetter().consume(port);
		
		addContacts();
	}


	private void addContacts() {
		addConnectionTo("Bamboo", "????");
		addConnectionTo("Bihaiko", "????");
		addConnectionTo("Daniel", "dfcsantos.homelinux.com");
		addConnectionTo("Klaus", "klaus.selfip.net", 5923);
		addConnectionTo("Klaus", "200.169.90.89", 5923);
		addConnectionTo("Localhost", "localhost");
	}
	

	private void addConnectionTo(String nick, String host) {
		addConnectionTo(nick, host, 7777);
	}
	
	
	private void addConnectionTo(String nick, String host, int port) {
		if (nick.equals(_ownName.name().currentValue())) return;
		
		Contact contact = produceContact(nick);
		_addressKeeper.add(contact, host, port);
	}


	private PublicKey mickeyMouseKey(String nick) {
		return _keyManager.generateMickeyMouseKey(nick);
	}


	private Contact produceContact(String nick) {
		Contact result = _contactManager.contactGiven(nick);
		if (result != null) return result;
		
		result = _contactManager.addContact(nick);
		_keyManager.addKey(result, mickeyMouseKey(nick));
		return result;
	}

}
