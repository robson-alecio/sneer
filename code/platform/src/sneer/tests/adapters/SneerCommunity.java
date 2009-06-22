package sneer.tests.adapters;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import sneer.bricks.pulp.network.ByteArrayServerSocket;
import sneer.bricks.pulp.network.ByteArraySocket;
import sneer.bricks.pulp.network.Network;
import sneer.foundation.brickness.Brickness;
import sneer.foundation.brickness.SneerHome;
import sneer.foundation.brickness.impl.EagerClassLoader;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.EnvironmentUtils;
import sneer.tests.SovereignCommunity;
import sneer.tests.SovereignParty;
import sneer.tests.utils.network.InProcessNetwork;


public class SneerCommunity implements SovereignCommunity {

	private final Network _network = new InProcessNetwork();
	private int _nextPort = 10000;

	private final File _tmpDirectory;
	
	public SneerCommunity(File tmpDirectory) {
		_tmpDirectory = tmpDirectory;
	}
	
	@Override
	public SovereignParty createParty(final String name) {
		SneerHome sneerHome = sneerHomeFor(name);
		File ownBinDirectory = ownBinDirectoryIn(sneerHome.get());
		
		Environment container = newContainer(sneerHome);
		URLClassLoader apiClassLoader = apiClassLoader(ownBinDirectory);
		
		Object partyImpl = EnvironmentUtils.retrieveFrom(container, loadProbeClassUsing(apiClassLoader));
		final SneerParty party = (SneerParty)ProxyInEnvironment.newInstance(container, partyImpl);
		
		party.setOwnName(name);
		party.setSneerPort(_nextPort++);
		party.setOwnBinDirectory(ownBinDirectory);
		return party;
	}


	private File ownBinDirectoryIn(String sneerHome) {
		return makeDirectory(new File(sneerHome), "own/bin");
	}

	private File makeDirectory(File parent, String child) {
		File result = new File(parent, child);
		if (!result.mkdirs())
			throw new IllegalStateException("Could not create directory '" + result + "'!");
		return result;
	}

	private Class<?> loadProbeClassUsing(URLClassLoader apiClassLoader) {
		try {
			return apiClassLoader.loadClass(SneerPartyProbe.class.getName());
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

	private Environment newContainer(SneerHome storagePath) {
		return Brickness.newBrickContainer(_network, storagePath);
	}

	private SneerHome sneerHomeFor(String name) {
		final File rootDirectory = rootDirectory(name);
		return new SneerHome() { @Override public String get() {
			return rootDirectory.getAbsolutePath();
		}};
	}

	private URLClassLoader apiClassLoader(File binDir) {
		return new EagerClassLoader(new URL[]{toURL(binDir), toURL(ClassFiles.classpathRootFor(SneerCommunity.class))}, SneerCommunity.class.getClassLoader()) {
			@Override
			protected boolean isEagerToLoad(String className) {
				return !isSharedByAllParties(className);
			}

			private boolean isSharedByAllParties(String className) {
				if (className.contains("xstream")) return true; System.out.println("Sandro, delete this line after XStream is made into a brick.");

				if (isNetworkClass(className)) return true;
				if (className.equals(SneerPartyProbe.class.getName())) return false;
				if (isPublishedByUser(className)) return false;
				return !isBrick(className); //Foundation classes such as Environments and functional tests classes such as SovereignParty mush be shared by all SneerParties.
			}

			private boolean isBrick(String className) {
				return className.startsWith("sneer.bricks");
			}

			private boolean isNetworkClass(String className) {
				if (className.equals(Network.class.getName())) return true;
				if (className.equals(ByteArrayServerSocket.class.getName())) return true;
				if (className.equals(ByteArraySocket.class.getName())) return true;
				return false;
			}

			private boolean isPublishedByUser(String className) {
				return !className.startsWith("sneer");
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
		return makeDirectory(_tmpDirectory, home);
	}

}
