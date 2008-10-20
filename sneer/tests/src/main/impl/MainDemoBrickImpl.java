package main.impl;

import main.MainDemoBrick;
import snapps.blinkinglights.gui.BlinkingLightsGui;
import snapps.contacts.gui.ContactsGui;
import snapps.share.gui.ShareGui;
import snapps.watchme.gui.RemoteWatchMeWindows;
import snapps.wind.gui.WindGui;
import sneer.kernel.container.Inject;
import sneer.pulp.clockticker.ClockTicker;
import sneer.pulp.connection.SocketOriginator;
import sneer.pulp.connection.SocketReceiver;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.pulp.dyndns.client.DynDnsClient;
import sneer.pulp.dyndns.ownaccount.DynDnsAccount;
import sneer.pulp.dyndns.ownaccount.DynDnsAccountKeeper;
import sneer.pulp.internetaddresskeeper.InternetAddressKeeper;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.own.name.OwnNameKeeper;
import sneer.pulp.port.PortKeeper;
import sneer.pulp.probe.ProbeManager;
import sneer.skin.dashboard.Dashboard;

class MainDemoBrickImpl implements MainDemoBrick {

	@Inject	@SuppressWarnings("unused")
	private static Dashboard _gui1;
	
	@Inject	@SuppressWarnings("unused")
	private static ContactsGui _gui2;
	
	@Inject	@SuppressWarnings("unused")
	private static BlinkingLightsGui _gui3;

	@Inject	@SuppressWarnings("unused")
	private static WindGui _gui4;
	
	@Inject	@SuppressWarnings("unused")
	private static ShareGui _gui5;

	@Inject	@SuppressWarnings("unused")
	private static RemoteWatchMeWindows _gui6;

	@Inject	@SuppressWarnings("unused")
	private static SocketOriginator _networkDaemon1;
	
	@Inject	@SuppressWarnings("unused")
	private static SocketReceiver _networkDaemon2;

	@Inject	@SuppressWarnings("unused")
	private static ClockTicker _ticker;

	@Inject	@SuppressWarnings("unused")
	private static DynDnsClient _dynDns;

	@Inject	@SuppressWarnings("unused")
	private static ProbeManager _probes;

	@Inject
	private static DynDnsAccountKeeper _dynDnsAccountKeeper;

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
	public void start(String ownName, String dynDnsUserName, String dynDnsPassword) {
		_ownName.nameSetter().consume(ownName);

		_portKeeper.portSetter().consume(portFor(ownName));
		
		initDynDnsAccount(ownName, dynDnsUserName, dynDnsPassword);
		
		addContacts();
	}

	private void initDynDnsAccount(String ownName, String dynDnsUserName, String dynDnsPassword) {
		if (dynDnsUserName == null) return;
		
		_dynDnsAccountKeeper.accountSetter().consume(
			new DynDnsAccount(dynDnsHostFor(ownName), dynDnsUserName, dynDnsPassword));
	}

	private String dynDnsHostFor(String ownName) {
		return contactInfoFor(ownName)._host;
	}

	private Integer portFor(String ownName) {
		return contactInfoFor(ownName)._port;
	}
	
	private ContactInfo contactInfoFor(String ownName) {
		for (ContactInfo candidate : contacts())
			if (candidate._nick.equals(ownName)) return candidate;
		throw new IllegalArgumentException(ownName + " is not one of the hardcoded names. If you want to use this Alpha version of Sneer please let us know at sneercoders@googlegroups.com and we will hardcode your name for now.");
	}
	

	private void addContacts() {
		for (ContactInfo contact : contacts()) {
			if (contact._nick.equals(ownName())) continue;
			addConnections(contact);
		}
	}

	private void addConnections(ContactInfo contact) {
		String nick = contact._nick;
		addConnectionTo(nick, contact._host, contact._port);
		
		for (String host : alternativeHostsFor(nick))
			addConnectionTo(nick, host, contact._port);
	}

	private String[] alternativeHostsFor(String nick) {
		if (nick.equals("Klaus")) return new String[]{"200.169.90.89", "10.42.11.19"};
		return new String[]{};
	}

	private ContactInfo[] contacts() {
		return new ContactInfo[] {
			new ContactInfo("Bamboo", "rodrigobamboo.dyndns.org", 5923),
			new ContactInfo("Bihaiko", "bihaiko.dyndns.org", 5923),
			new ContactInfo("Daniel", "dfcsantos.homelinux.com", 7777),
			new ContactInfo("Douglas", "dtgiacomini.dyndns.org", 5923),
			new ContactInfo("Duno", "duno.dyndns.org", 5923),
			new ContactInfo("Klaus", "klausw.selfip.net", 5923),
			new ContactInfo("Localhost", "localhost", 7777),
			new ContactInfo("Nell", "anelisedaux.dyndns.org", 5924),
			new ContactInfo("Priscila", "priscilavriesman.dyndns.org", 7770),
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
		_addressKeeper.add(contact, host, port);
	}

	@SuppressWarnings("deprecation")
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

	private String ownName() {
		return _ownName.name().currentValue();
	}


}
