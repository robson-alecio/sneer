package sneer.tests.adapters.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.util.List;

import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.hardware.cpu.lang.Lang;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.ram.iterables.Iterables;
import sneer.bricks.pulp.connection.ByteConnection;
import sneer.bricks.pulp.connection.ConnectionManager;
import sneer.bricks.pulp.contacts.Contact;
import sneer.bricks.pulp.contacts.ContactManager;
import sneer.bricks.pulp.internetaddresskeeper.InternetAddressKeeper;
import sneer.bricks.pulp.keymanager.KeyManager;
import sneer.bricks.pulp.own.name.OwnNameKeeper;
import sneer.bricks.pulp.port.PortKeeper;
import sneer.bricks.snapps.wind.Shout;
import sneer.bricks.snapps.wind.Wind;
import sneer.bricks.software.bricks.Bricks;
import sneer.bricks.software.directoryconfig.DirectoryConfig;
import sneer.foundation.brickness.PublicKey;
import sneer.foundation.lang.exceptions.NotImplementedYet;
import sneer.foundation.lang.exceptions.Refusal;
import sneer.tests.SovereignParty;
import sneer.tests.adapters.SneerParty;
import sneer.tests.adapters.SneerPartyProbe;

class SneerPartyProbeImpl implements SneerPartyProbe, SneerParty {
	
	static private final String MOCK_ADDRESS = "localhost";

	private final Clock _clock = my(Clock.class);

	private final ContactManager _contactManager = my(ContactManager.class);

	private final ConnectionManager _connectionManager = my(ConnectionManager.class);
	
	private final PortKeeper _sneerPortKeeper = my(PortKeeper.class);
	
	private final OwnNameKeeper _ownNameKeeper = my(OwnNameKeeper.class);
	
	private final InternetAddressKeeper _internetAddressKeeper = my(InternetAddressKeeper.class);

	private final Wind _wind = my(Wind.class);

	private final KeyManager _keyManager = my(KeyManager.class);
	
	{
		my(sneer.bricks.pulp.log.receiver.sysout.LogToSysout.class);
		my(sneer.bricks.pulp.connection.SocketOriginator.class);
		my(sneer.bricks.pulp.connection.SocketReceiver.class);
		my(sneer.bricks.pulp.probe.ProbeManager.class);
		my(sneer.bricks.hardware.clock.ticker.ClockTicker.class);
	}
	

	@Override
	public void setSneerPort(int port) {
		try {
			_sneerPortKeeper.portSetter().consume(port);
		} catch (Refusal e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public void bidirectionalConnectTo(SovereignParty party) {
		Contact contact = addContact(party.ownName());

		SneerParty sneerParty = (SneerParty)party;
		storePublicKey(contact, new PublicKey(sneerParty.publicKey()));
		_internetAddressKeeper.add(contact, MOCK_ADDRESS, sneerParty.sneerPort());
		
		sneerParty.giveNicknameTo(this, this.ownName());
		waitUntilOnline(contact);
	}

	private void storePublicKey(Contact contact, PublicKey publicKey) {
		_keyManager.addKey(contact, publicKey);
	}

	private Contact addContact(String nickname) {
		try {
			return _contactManager.addContact(nickname);
		} catch (Refusal e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public String ownName() {
		return _ownNameKeeper.name().currentValue();
	}

	@Override
	public void setOwnName(String newName) {
		_ownNameKeeper.nameSetter().consume(newName);
	}
	
    @Override
    public void giveNicknameTo(SovereignParty peer, String newNickname) {
    	byte[] publicKey = ((SneerParty)peer).publicKey();
		Contact contact = waitForContactGiven(publicKey);

		try {
			_contactManager.nicknameSetterFor(contact).consume(newNickname);
		} catch (Refusal e) {
			throw new IllegalStateException(e);
		}
		
		waitUntilOnline(contact);

    }

	private void waitUntilOnline(Contact contact) {
		ByteConnection connection = _connectionManager.connectionFor(contact);
		
		while (!connection.isOnline().currentValue())
			my(Threads.class).sleepWithoutInterruptions(1);
	}

	private Contact waitForContactGiven(byte[] publicKey) {
		while (true) {
			Contact contact = _keyManager.contactGiven(new PublicKey(publicKey));
			if (contact != null) return contact;
			my(Threads.class).sleepWithoutInterruptions(1);
			_clock.advanceTime(60 * 1000);
		}
	}

	@Override
    public byte[] publicKey() {
		return _keyManager.ownPublicKey().bytes();
	}

	@Override
    public void navigateAndWaitForName(String nicknamePath, String expectedName) {
		//nicknamePath.split("/")
		//my(SignalUtils.class).waitForValue() might be useful.
		throw new NotImplementedYet();
    }


	@Override
	public int sneerPort() {
        return _sneerPortKeeper.port().currentValue();
    }


	@Override
	public void publishBricks(File sourceDirectory) {
		my(Bricks.class).publish(sourceDirectory);
	}


	@Override
	public void shout(String phrase) {
		_wind.megaphone().consume(phrase);
	}

	@Override
	public void waitForShouts(String shoutsExpected) {
		while (true) {
			String shoutsHeard = concat(_wind.shoutsHeard());
			if (shoutsHeard.equals(shoutsExpected)) return;
			try {
				Thread.sleep(200);
			} catch (InterruptedException ignored) {
				throw new RuntimeException(ownName() + " was waiting for: " + shoutsExpected + "  was still: " + shoutsHeard);
			}
		}
	}

	private String concat(Iterable<Shout> shouts) {
		List<Shout> sorted = my(Iterables.class).sortByToString(shouts);
		return my(Lang.class).strings().join(sorted, ", ");
	}

	@Override
	public void setOwnBinDirectory(File ownBinDirectory) {
		my(DirectoryConfig.class).ownBinDirectory().set(ownBinDirectory);
	}

}

