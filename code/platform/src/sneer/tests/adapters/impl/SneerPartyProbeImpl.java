package sneer.tests.adapters.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.hardware.cpu.lang.Lang;
import sneer.bricks.hardware.cpu.lang.contracts.Contract;
import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.ram.iterables.Iterables;
import sneer.bricks.network.social.Contact;
import sneer.bricks.network.social.ContactManager;
import sneer.bricks.network.social.heartbeat.stethoscope.Stethoscope;
import sneer.bricks.pulp.internetaddresskeeper.InternetAddressKeeper;
import sneer.bricks.pulp.keymanager.Seals;
import sneer.bricks.pulp.own.name.OwnNameKeeper;
import sneer.bricks.pulp.port.PortKeeper;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.SignalUtils;
import sneer.bricks.snapps.wind.Shout;
import sneer.bricks.snapps.wind.Wind;
import sneer.bricks.software.bricks.Bricks;
import sneer.bricks.software.directoryconfig.DirectoryConfig;
import sneer.bricks.softwaresharing.BrickInfo;
import sneer.bricks.softwaresharing.BrickUniverse;
import sneer.bricks.softwaresharing.BrickVersion;
import sneer.bricks.softwaresharing.publisher.BrickPublisher;
import sneer.foundation.brickness.Seal;
import sneer.foundation.lang.Predicate;
import sneer.foundation.lang.exceptions.NotImplementedYet;
import sneer.foundation.lang.exceptions.Refusal;
import sneer.tests.SovereignParty;
import sneer.tests.adapters.SneerParty;
import sneer.tests.adapters.SneerPartyProbe;

class SneerPartyProbeImpl implements SneerPartyProbe, SneerParty {
	
	static private final String MOCK_ADDRESS = "localhost";
	private Collection<Object> _referenceToAvoidGc = new ArrayList<Object>();
	@SuppressWarnings("unused")	private Contract _referenceToAvoicGc2;

	@Override
	public void setSneerPort(int port) {
		try {
			my(PortKeeper.class).portSetter().consume(port);
		} catch (Refusal e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public void connectTo(SneerParty party) {
		Contact contact = my(ContactManager.class).produceContact(party.ownName());

		SneerParty sneerParty = party;
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
    	byte[] publicKey = peer.seal();
		Contact contact = waitForContactGiven(publicKey);

		try {
			my(ContactManager.class).nicknameSetterFor(contact).consume(newNickname);
		} catch (Refusal e) {
			throw new IllegalStateException(e);
		}
		
		waitUntilOnline(contact);
    }

	private void waitUntilOnline(Contact contact) {
		//System.out.println("WAITING FOR ONLINE: " + contact.nickname().currentValue() + " " + contact);
		my(SignalUtils.class).waitForValue(isAlive(contact), true);
	}

	private Signal<Boolean> isAlive(Contact contact) {
		return my(Stethoscope.class).isAlive(contact);
	}

	private Contact waitForContactGiven(byte[] publicKey) {
		while (true) {
			Contact contact = my(Seals.class).contactGiven(new Seal(publicKey));
			if (contact != null) return contact;
			my(Threads.class).sleepWithoutInterruptions(10);
			my(Clock.class).advanceTime(60 * 1000);
		}
	}

	@Override
    public byte[] seal() {
		return my(Seals.class).ownSeal().bytes();
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
	public void installBricks(File sourceDirectory) throws IOException {
		my(Bricks.class).install(sourceDirectory);
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
	public void configDirectories(File dataDirectory, File ownSrcDirectory, File ownBinDirectory, File platformSrcDirectory, File platformBinDirectory) {
		my(DirectoryConfig.class).dataDirectory().set(dataDirectory);
		my(DirectoryConfig.class).ownSrcDirectory().set(ownSrcDirectory);
		my(DirectoryConfig.class).ownBinDirectory().set(ownBinDirectory);
		my(DirectoryConfig.class).platformSrcDirectory().set(platformSrcDirectory);
		my(DirectoryConfig.class).platformBinDirectory().set(platformBinDirectory);
	}


	@Override
	public void startSnapps() {
		startAndKeep(sneer.bricks.snapps.system.log.sysout.LogToSysout.class);
		startAndKeep(sneer.bricks.network.computers.sockets.connections.originator.SocketOriginator.class);
		startAndKeep(sneer.bricks.network.computers.sockets.connections.receiver.SocketReceiver.class);
		startAndKeep(sneer.bricks.pulp.probe.ProbeManager.class);
		startAndKeep(sneer.bricks.snapps.wind.Wind.class);
		startAndKeep(sneer.bricks.softwaresharing.BrickUniverse.class);
		startAndKeep(sneer.bricks.network.social.heartbeat.stethoscope.Stethoscope.class);
		startAndKeep(sneer.bricks.network.social.heartbeat.Heart.class);
	}

	private void startAndKeep(Class<?> snapp) {
		_referenceToAvoidGc.add(my(snapp));
	}

	@Override
	public boolean isOnline(String nickname) {
		Contact contact = my(ContactManager.class).contactGiven(nickname);
		return isAlive(contact).currentValue();
	}

	@Override
	public void accelerateHeartbeat() {
		_referenceToAvoicGc2 = my(Threads.class).keepStepping(new Steppable() { @Override public void step() {
			my(Clock.class).advanceTime(10 * 1000);
			my(Threads.class).sleepWithoutInterruptions(500);
		}});
	}

	@Override
	public void waitForAvailableBrick(final String brickName) {
		my(SignalUtils.class).waitForElement(my(BrickUniverse.class).availableBricks(), new Predicate<BrickInfo>() { @Override public boolean evaluate(BrickInfo brickInfo) {
			return brickInfo.name().equals(brickName);
		}});
	}
	
	@Override
	public void installTheOnlyAvailableVersionOfBrick(String brickName) throws IOException {
		final BrickInfo brick = availableBrick(brickName);
		final BrickVersion latestVersion = onlyVersionOf(brick);
		installBricks(latestVersion.sourceFolder());
	}

	private BrickVersion onlyVersionOf(BrickInfo brick) {
		if (brick.versions().size() != 1)
			throw new IllegalStateException();
		return brick.versions().get(0);
	}

	private BrickInfo availableBrick(String brickName) {
		for (BrickInfo brick : my(BrickUniverse.class).availableBricks())
			if (brick.name().equals(brickName))
				return brick;
		throw new IllegalArgumentException();
	}

	@Override
	public void publishBrick(String brickName) throws IOException {
		my(BrickPublisher.class).publishBrick(brickName);
	}

}

