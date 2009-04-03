package functional.adapters;

import java.io.File;

import main.Sneer;

import org.apache.commons.lang.StringUtils;

import sneer.commons.environments.Environments;
import sneer.commons.io.StoragePath;
import sneer.container.Brickness;
import sneer.container.impl.BricknessImpl;
import sneer.kernel.container.SneerConfig;
import sneer.pulp.network.Network;
import testutils.network.InProcessNetwork;
import wheel.io.Jars;
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
		Brickness container = newContainer(name);
		populate(container);
		final SneerParty party = Environments.wrap(SneerParty.class, container.environment());
		party.setOwnName(name);
		party.setSneerPort(_nextPort++);
		return party;
	}

	private void populate(Brickness container) {
		try {
			runBricks(container, Sneer.businessBricks());
			runBricks(container, SneerParty.class);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	private void runBricks(Brickness container, Class<?>... bricks) {
		for (Class<?> brick : bricks)
			container.runBrick(Jars.classpathRootFor(brick), brick.getName());
	}

	private Brickness newContainer(final String name) {
		StoragePath storagePath = new StoragePath() { @Override public String get() {
			return sneerConfigForParty(name).sneerDirectory().getAbsolutePath();
		}};
		
		return new BricknessImpl(_network, sneerConfigForParty(name), storagePath);
	}

	private SneerConfig sneerConfigForParty(String name) {
		File root = rootDirectory(name);
		SneerConfig config = new SneerConfigMock(root);
		return config;
	}

	private File rootDirectory(String name) {
		String fileName = ".sneer-"+StringUtils.deleteWhitespace(name);
		return new File(_tmpDirectory, fileName);
		
	}

}
