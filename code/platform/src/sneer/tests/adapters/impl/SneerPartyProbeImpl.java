package sneer.tests.adapters.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.util.List;

import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.hardware.cpu.lang.Lang;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.ram.iterables.Iterables;
import sneer.bricks.network.computers.sockets.connections.ByteConnection;
import sneer.bricks.network.computers.sockets.connections.ConnectionManager;
import sneer.bricks.network.social.Contact;
import sneer.bricks.network.social.ContactManager;
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

	@Override
	public void setSneerPort(int port) {
		try {
			my(PortKeeper.class).portSetter().consume(port);
		} catch (Refusal e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public void connectTo(SovereignParty party) {
		Contact contact = my(ContactManager.class).produceContact(party.ownName());

		SneerParty sneerParty = (SneerParty)party;
		//storePublicKey(contact, new PublicKey(sneerParty.publicKey()));
		my(InternetAddressKeeper.class).add(contact, MOCK_ADDRESS, sneerParty.sneerPort());
		
		waitUntilOnline(contact);
	}

//	private void storePublicKey(Contact contact, PublicKey publicKey) {
//		_keyManager.addKey(contact, publicKey);
//	}


	@Override
	public String ownName() {
		return my(OwnNameKeeper.class).name().currentValue();
	}

	@Override
	public void setOwnName(String newName) {
		my(OwnNameKeeper.class).nameSetter().consume(newName);
	}
	
    @Override
    public void giveNicknameTo(SovereignParty peer, String newNickname) {
    	byte[] publicKey = ((SneerParty)peer).publicKey();
		Contact contact = waitForContactGiven(publicKey);

		try {
			my(ContactManager.class).nicknameSetterFor(contact).consume(newNickname);
		} catch (Refusal e) {
			throw new IllegalStateException(e);
		}
		
		waitUntilOnline(contact);

    }

	private void waitUntilOnline(Contact contact) {
		ByteConnection connection = my(ConnectionManager.class).connectionFor(contact);
		
		while (!connection.isOnline().currentValue())
			my(Threads.class).sleepWithoutInterruptions(10);
	}

	private Contact waitForContactGiven(byte[] publicKey) {
		while (true) {
			Contact contact = my(KeyManager.class).contactGiven(new PublicKey(publicKey));
			if (contact != null) return contact;
			my(Threads.class).sleepWithoutInterruptions(10);
			my(Clock.class).advanceTime(60 * 1000);
		}
	}

	@Override
    public byte[] publicKey() {
		return my(KeyManager.class).ownPublicKey().bytes();
	}

	@Override
    public void navigateAndWaitForName(String nicknamePath, String expectedName) {
		//nicknamePath.split("/")
		//my(SignalUtils.class).waitForValue() might be useful.
		throw new NotImplementedYet();
    }


	@Override
	public int sneerPort() {
        return my(PortKeeper.class).port().currentValue();
    }


	@Override
	public void publishBricks(File sourceDirectory) {
		my(Bricks.class).publish(sourceDirectory);
	}


	@Override
	public void shout(String phrase) {
		my(Wind.class).megaphone().consume(phrase);
	}

	@Override
	public void waitForShouts(String shoutsExpected) {
		while (true) {
			String shoutsHeard = concat(my(Wind.class).shoutsHeard());
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

	@Override
	public void setDataDirectory(File dataDirectory) {
		my(DirectoryConfig.class).dataDirectory().set(dataDirectory);
	}

	@Override
	public void startSnapps() {
		my(sneer.bricks.snapps.system.log.sysout.LogToSysout.class);
		my(sneer.bricks.network.computers.sockets.connections.originator.SocketOriginator.class);
		my(sneer.bricks.network.computers.sockets.connections.receiver.SocketReceiver.class);
		my(sneer.bricks.pulp.probe.ProbeManager.class);
		my(sneer.bricks.hardware.clock.ticker.ClockTicker.class);
	}

	@Override
	public boolean isOnline(String nickname) {
		Contact contact = my(ContactManager.class).contactGiven(nickname);
		return my(ConnectionManager.class).connectionFor(contact).isOnline().currentValue();
	}

}

