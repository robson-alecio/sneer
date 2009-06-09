package functional.adapters;

import static sneer.commons.environments.Environments.my;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import main.SneerSession;
import sneer.brickness.Brickness;
import sneer.brickness.StoragePath;
import sneer.commons.environments.Environment;
import sneer.commons.environments.EnvironmentUtils;
import sneer.hardware.cpu.lang.Lang;
import sneer.pulp.network.Network;
import testutils.network.InProcessNetwork;
import functional.SovereignCommunity;
import functional.SovereignParty;


public class SneerCommunity implements SovereignCommunity {

	private final Network _network = new InProcessNetwork();
	private int _nextPort = 10000;

	private final File _tmpDirectory;
	
	public SneerCommunity(File tmpDirectory) {
		_tmpDirectory = tmpDirectory;
	}
	
	@Override
	public SovereignParty createParty(final String name) {
		Environment container = newContainer(name);
		loadBricks(container);
		
		final SneerParty party = ProxyInEnvironment.newInstance(SneerParty.class, container);
		party.setOwnName(name);
		party.setSneerPort(_nextPort++);
		return party;
	}

	private void loadBricks(Environment container) {
		try {
			loadBricks(container, SneerSession.platformBricks());
			loadBricks(container, SneerParty.class);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	private void loadBricks(Environment container, final Class<?>... bricks) {
		for (Class<?> brick : bricks)
			EnvironmentUtils.retrieveFrom(container, brick);
	}

	private Environment newContainer(final String name) {
		final File rootDirectory = rootDirectory(name);
		
		StoragePath storagePath = new StoragePath() { @Override public String get() {
			File result = rootDirectory;
			if (!result.exists()) result.mkdirs();
			return result.getAbsolutePath();
		}};
		
		return Brickness.newBrickContainerWithApiClassLoader(apiClassLoader(rootDirectory), _network, storagePath);
	}

	private URLClassLoader apiClassLoader(File rootDirectory) {
		File binDir = new File(rootDirectory.getAbsolutePath(),"bin");
		if (!binDir.exists() && !binDir.mkdirs())
			throw new IllegalStateException("Could not create temporary directory '" + binDir + "'!");

		URL url;
		try {
			url = binDir.toURI().toURL();
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
		return new URLClassLoader(new URL[]{url}, SneerCommunity.class.getClassLoader());
	}

	private File rootDirectory(String name) {
		String fileName = ".sneer-"+my(Lang.class).strings().deleteWhitespace(name);
		return new File(_tmpDirectory, fileName);
		
	}

}
