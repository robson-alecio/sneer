package main.impl;

import static sneer.commons.environments.Environments.my;
import main.MainSneerBrick;
import snapps.blinkinglights.gui.BlinkingLightsGui;
import snapps.contacts.gui.ContactsGui;
import snapps.meter.bandwidth.gui.BandwidthMeterGui;
import snapps.meter.memory.gui.MemoryMeterGui;
import snapps.watchme.gui.WatchMeGui;
import snapps.watchme.gui.windows.RemoteWatchMeWindows;
import snapps.whisper.gui.WhisperGui;
import snapps.whisper.speextuples.SpeexTuples;
import snapps.wind.gui.WindGui;
import sneer.brickness.PublicKey;
import sneer.pulp.clockticker.ClockTicker;
import sneer.pulp.connection.SocketOriginator;
import sneer.pulp.connection.SocketReceiver;
import sneer.pulp.connection.reachability.ReachabilitySentinel;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.pulp.dyndns.client.DynDnsClient;
import sneer.pulp.internetaddresskeeper.InternetAddressKeeper;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.logging.Logger;
import sneer.pulp.own.name.OwnNameKeeper;
import sneer.pulp.probe.ProbeManager;
import sneer.skin.dashboard.Dashboard;

class MainSneerBrickImpl implements MainSneerBrick {

	@SuppressWarnings("unused")
	private final ClockTicker _ticker = my(ClockTicker.class);

	@SuppressWarnings("unused")
	private final Dashboard _gui1 = my(Dashboard.class);
	
	@SuppressWarnings("unused")
	private final ContactsGui _gui2 = my(ContactsGui.class);
	
	@SuppressWarnings("unused")
	private final WindGui _gui3 = my(WindGui.class);
	
	@SuppressWarnings("unused")
	private final WatchMeGui _gui4 = my(WatchMeGui.class);

	@SuppressWarnings("unused")
	private final WhisperGui _gui5 = my(WhisperGui.class);

	@SuppressWarnings("unused")
	private final RemoteWatchMeWindows _gui6 = my(RemoteWatchMeWindows.class);

	@SuppressWarnings("unused")
	private final MemoryMeterGui _gui7 = my(MemoryMeterGui.class);

	@SuppressWarnings("unused")
	private final BandwidthMeterGui _gui8 = my(BandwidthMeterGui.class);

	@SuppressWarnings("unused")
	private final BlinkingLightsGui _lastGui = my(BlinkingLightsGui.class);
	
	@SuppressWarnings("unused")
	private final SocketOriginator _networkDaemon1 = my(SocketOriginator.class);
	
	@SuppressWarnings("unused")
	private final SocketReceiver _networkDaemon2 = my(SocketReceiver.class);

	@SuppressWarnings("unused")
	private final DynDnsClient _dynDns = my(DynDnsClient.class);
	
	@SuppressWarnings("unused")
	private final ReachabilitySentinel _reachabilitySentinel = my(ReachabilitySentinel.class);	
	
	@SuppressWarnings("unused")
	private final SpeexTuples _speexTuples = my(SpeexTuples.class);	

	@SuppressWarnings("unused")
	private final ProbeManager _probes = my(ProbeManager.class);


	
	private final ContactManager _contactManager = my(ContactManager.class);

	private final KeyManager _keyManager = my(KeyManager.class);
	
	private final InternetAddressKeeper _addressKeeper = my(InternetAddressKeeper.class);

	
	{
		my(Logger.class).enterRobustMode();

		WelcomeWizard.showIfNecessary();
		
		addContacts();
		
		my(Dashboard.class).show();
	}

	
	
	
	
	private void addContacts() {
		for (ContactInfo contact : contacts()) {
			if (contact._nick.equals(ownName())) continue;
			addConnections(contact);
		}
	}

	private Object ownName() {
		return my(OwnNameKeeper.class).name().currentValue();
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
			new ContactInfo("agnaldo4j", "agnaldo4j.selfip.com", 5923),
			new ContactInfo("Bamboo", "rodrigobamboo.dyndns.org", 5923),
			new ContactInfo("Bihaiko", "bihaiko.dyndns.org", 5923),
			new ContactInfo("Daniel", "dfcsantos.homelinux.com", 7777),
			new ContactInfo("Douglas", "dtgiacomini.dyndns.org", 5923),
			new ContactInfo("Dummy", "localhost", 7777),
			new ContactInfo("Duno", "duno.dyndns.org", 5923),
			new ContactInfo("Kalecser", "kalecser.dyndns.org", 7770),
			new ContactInfo("Klaus", "klausw.selfip.net", 5923),
			new ContactInfo("Nell", "anelisedaux.dyndns.org", 5924),
			new ContactInfo("Priscila", "priscilavriesman.dyndns.org", 7770),
			new ContactInfo("Ramon", "ramontramontini.dyndns.org", 7770),
			new ContactInfo("Vitor", "vfpamp.dyndns.org", 5923),
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

	
	
	
	
	
	
	
	
	
	
	
	






}
