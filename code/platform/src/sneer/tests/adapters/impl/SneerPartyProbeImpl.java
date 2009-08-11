package sneer.tests.adapters.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.hardware.cpu.lang.Lang;
import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.hardware.cpu.threads.Latch;
import sneer.bricks.hardware.cpu.threads.Steppable;
import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.io.IO;
import sneer.bricks.hardware.ram.iterables.Iterables;
import sneer.bricks.hardwaresharing.files.server.FileServer;
import sneer.bricks.network.computers.sockets.connections.originator.SocketOriginator;
import sneer.bricks.network.computers.sockets.connections.receiver.SocketReceiver;
import sneer.bricks.network.social.Contact;
import sneer.bricks.network.social.ContactManager;
import sneer.bricks.network.social.heartbeat.Heart;
import sneer.bricks.network.social.heartbeat.stethoscope.Stethoscope;
import sneer.bricks.network.social.loggers.tuples.TupleLogger;
import sneer.bricks.pulp.internetaddresskeeper.InternetAddressKeeper;
import sneer.bricks.pulp.keymanager.Seals;
import sneer.bricks.pulp.own.name.OwnNameKeeper;
import sneer.bricks.pulp.port.PortKeeper;
import sneer.bricks.pulp.probe.ProbeManager;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.SignalUtils;
import sneer.bricks.snapps.wind.Shout;
import sneer.bricks.snapps.wind.Wind;
import sneer.bricks.software.folderconfig.FolderConfig;
import sneer.bricks.softwaresharing.BrickInfo;
import sneer.bricks.softwaresharing.BrickSpace;
import sneer.bricks.softwaresharing.BrickVersion;
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
		Contact contact = produceContact(party.ownName());

		SneerParty sneerParty = party;
		//storePublicKey(contact, new PublicKey(sneerParty.publicKey()));
		my(InternetAddressKeeper.class).add(contact, MOCK_ADDRESS, sneerParty.sneerPort());

		waitUntilOnline(contact);
	}

	private Contact produceContact(String contactName) {
		return my(ContactManager.class).produceContact(contactName);
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
    
    @Override
    public void waitUntilOnline(String nickname) {
    	waitUntilOnline(produceContact(nickname));
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
	public void shout(String phrase) {
		my(Wind.class).megaphone().consume(phrase);
	}

	@Override
	public void waitForShouts(final String shoutsExpected) {
		final Latch latch = my(Threads.class).newLatch();

		WeakContract contract = my(Wind.class).shoutsHeard().addPulseReceiver(new Runnable() { @Override public void run() {
			openLatchIfShoutsHeard(shoutsExpected, latch);
		}});
		openLatchIfShoutsHeard(shoutsExpected, latch);
		
		latch.waitTillOpen();
		contract.dispose();
	}

	private void openLatchIfShoutsHeard( String shoutsExpected, Latch latch) {
		String shoutsHeard = concat(my(Wind.class).shoutsHeard());
		if (shoutsHeard.equals(shoutsExpected))
			latch.open();
	}

	private String concat(Iterable<Shout> shouts) {
		List<Shout> sorted = my(Iterables.class).sortByToString(shouts);
		return my(Lang.class).strings().join(sorted, ", ");
	}

	@Override
	public void configDirectories(File dataFolder, File platformSrcFolder, File platformBinFolder) {
		my(FolderConfig.class).dataFolder().set(dataFolder);
		my(FolderConfig.class).platformSrcFolder().set(platformSrcFolder);
		my(FolderConfig.class).platformBinFolder().set(platformBinFolder);
	}


	private void startSnapps() {
		startAndKeep(SocketOriginator.class);
		startAndKeep(SocketReceiver.class);
		startAndKeep(ProbeManager.class);

		startAndKeep(TupleLogger.class);

		startAndKeep(Wind.class);

		startAndKeep(FileServer.class);
		startAndKeep(BrickSpace.class);

		startAndKeep(Stethoscope.class);
		startAndKeep(Heart.class);
	}

	private void startAndKeep(Class<?> snapp) {
		_referenceToAvoidGc.add(my(snapp));
	}

	@Override
	public boolean isOnline(String nickname) {
		Contact contact = my(ContactManager.class).contactGiven(nickname);
		return isAlive(contact).currentValue();
	}

	private void accelerateHeartbeat() {
		my(Threads.class).startStepping(new Steppable() { @Override public void step() {
			my(Clock.class).advanceTime(10 * 1000);
			my(Threads.class).sleepWithoutInterruptions(100);
		}});
	}

	@Override
	public void waitForAvailableBrick(final String brickName) {
		my(SignalUtils.class).waitForElement(my(BrickSpace.class).availableBricks(), new Predicate<BrickInfo>() { @Override public boolean evaluate(BrickInfo brickInfo) {
			return brickInfo.name().equals(brickName);
		}});
	}
	
	@Override
	public void stageBrickForExecution(String brickName) {
		final BrickInfo brick = availableBrick(brickName);
		final BrickVersion singleVersion = singleVersionOf(brick);
		brick.setStagedForExecution(singleVersion, true);
	}

	private BrickVersion singleVersionOf(BrickInfo brick) {
		if (brick.versions().size() != 1)
			throw new IllegalStateException();
		return brick.versions().get(0);
	}

	private BrickInfo availableBrick(String brickName) {
		for (BrickInfo brick : my(BrickSpace.class).availableBricks())
			if (brick.name().equals(brickName))
				return brick;
		throw new IllegalArgumentException();
	}

	@Override
	public void crash() {
		my(Threads.class).crashAllThreads();
	}

	@Override
	public void start() {
		installStagedBricks();
		
		startSnapps();
		accelerateHeartbeat();
	}

	private void installStagedBricks() {
		try {
			tryToInstallStagedBricks();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@SuppressWarnings("unused")
	private void tryToInstallStagedBricks() throws IOException {
		for(BrickInfo brickInfo: my(BrickSpace.class).availableBricks())
			for (BrickVersion version : brickInfo.versions())
				if (version.isStagedForExecution())
					installStagedVersion(version);
	}

	private void installStagedVersion(@SuppressWarnings("unused") BrickVersion version) {
		//installBricks(tmpSrcFolderContainingFilesFor(version));
		throw new NotImplementedYet();
	}

	@Override
	public void copyToSourceFolder(File folderWithBricks) throws IOException {
		
		my(IO.class).files().copyFolder(folderWithBricks, platformSrcFolder());
	}

	private File platformSrcFolder() {
		return my(FolderConfig.class).platformSrcFolder().get();
	}

}

