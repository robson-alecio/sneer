package sneer.tests.adapters;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import sneer.bricks.pulp.events.EventSource;
import sneer.bricks.pulp.network.ByteArrayServerSocket;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.bricks.pulp.network.Network;
import sneer.bricks.pulp.reactive.Signal;
import sneer.bricks.pulp.reactive.collections.CollectionSignal;
import sneer.bricks.pulp.reactive.collections.ListSignal;
import sneer.foundation.brickness.Brick;
import sneer.foundation.brickness.Brickness;
import sneer.foundation.brickness.PublicKey;
import sneer.foundation.brickness.StoragePath;
import sneer.foundation.brickness.impl.EagerClassLoader;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.EnvironmentUtils;
import sneer.foundation.environments.Environments;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.PickyConsumer;
import sneer.foundation.testsupport.Daemon;
import sneer.tests.SovereignCommunity;
import sneer.tests.SovereignParty;
import sneer.tests.utils.network.InProcessNetwork;


@SuppressWarnings("deprecation")
public class SneerCommunity implements SovereignCommunity {

	private final Network _network = new InProcessNetwork();
	private int _nextPort = 10000;

	private final File _tmpDirectory;
	
	public SneerCommunity(File tmpDirectory) {
		_tmpDirectory = tmpDirectory;
	}
	
	@Override
	public SovereignParty createParty(final String name) {
		StoragePath storagePath = storagePath(name);
		Environment container = newContainer(storagePath);
		URLClassLoader apiClassLoader = apiClassLoader(storagePath.get());
		
		Object partyImpl = EnvironmentUtils.retrieveFrom(container, loadSneerPartyBrickClass(apiClassLoader));
		final SneerParty party = (SneerParty)ProxyInEnvironment.newInstance(container, partyImpl);
		
		party.setOwnName(name);
		party.setSneerPort(_nextPort++);
		return party;
	}

	private Class<?> loadSneerPartyBrickClass(URLClassLoader apiClassLoader) {
		try {
			return apiClassLoader.loadClass(SneerPartyBrick.class.getName());
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

	private Environment newContainer(StoragePath storagePath) {
		return Brickness.newBrickContainer(_network, storagePath);
	}

	private StoragePath storagePath(String name) {
		final File rootDirectory = rootDirectory(name);
		return new StoragePath() { @Override public String get() {
			return rootDirectory.getAbsolutePath();
		}};
	}

	private URLClassLoader apiClassLoader(String rootDirectory) {
		File binDir = new File(rootDirectory, "bin");
		if (!binDir.exists() && !binDir.mkdirs())
			throw new IllegalStateException("Could not create temporary directory '" + binDir + "'!");

		return new EagerClassLoader(new URL[]{toURL(binDir), toURL(ClassFiles.classpathRootFor(SneerCommunity.class))}, SneerCommunity.class.getClassLoader()) {
			@Override
			protected boolean isEagerToLoad(String className) {
				if (className.equals(SovereignParty.class.getName())) return false;
				if (className.equals(SneerParty.class.getName())) return false;
				if (className.equals(Brick.class.getName())) return false;
				if (className.equals(Environments.class.getName())) return false;
				if (className.equals(Environment.class.getName())) return false;
				if (className.equals(StoragePath.class.getName())) return false;
				if (className.equals(Network.class.getName())) return false;
				if (className.equals(ByteArrayServerSocket.class.getName())) return false;
				if (className.equals(ByteArraySocket.class.getName())) return false;

				//Refactor: Simplify SneerParty interface to use only JRE types:
				if (className.equals(Signal.class.getName())) return false;
				if (className.equals(ListSignal.class.getName())) return false;
				if (className.equals(CollectionSignal.class.getName())) return false;
				if (className.equals(EventSource.class.getName())) return false;
				if (className.equals(Consumer.class.getName())) return false;
				if (className.equals(PickyConsumer.class.getName())) return false;
				if (className.equals(PublicKey.class.getName())) return false;

				//Refactor: Use Threads brick.
				if (className.equals(Daemon.class.getName())) return false;

				//Refactor: Make into a brick:
				if (className.contains("xstream")) return false;

				return true;
			}
		};
	}

	private URL toURL(File file) {
		try {
			return file.toURI().toURL();
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
	}

	private File rootDirectory(String name) {
		String home = "sneer-" + name.replace(' ', '_');
		File result = new File(_tmpDirectory, home);
		if (!result.mkdirs()) throw new IllegalStateException();
		return result;
		
	}

}
