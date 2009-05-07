package functional.adapters;

import java.io.File;

import main.Sneer;

import org.apache.commons.lang.StringUtils;

import sneer.brickness.Brickness;
import sneer.brickness.BricknessFactory;
import sneer.brickness.StoragePath;
import sneer.brickness.testsupport.ClassFiles;
import sneer.commons.environments.Environments;
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
		Brickness container = newContainer(name);
		placeBricks(container);
		
		final SneerParty party = Environments.wrap(SneerParty.class, container.environment());
		party.setOwnName(name);
		party.setSneerPort(_nextPort++);
		return party;
	}

	private void placeBricks(Brickness container) {
		try {
			placeBricks(container, Sneer.businessBricks());
			placeBricks(container, SneerParty.class);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	private void placeBricks(Brickness container, Class<?>... bricks) {
		for (Class<?> brick : bricks)
			container.placeBrick(ClassFiles.classpathRootFor(brick), brick.getName());
	}

	private Brickness newContainer(final String name) {
		StoragePath storagePath = new StoragePath() { @Override public String get() {
			File result = rootDirectory(name);
			if (!result.exists()) result.mkdirs();
			return result.getAbsolutePath();
		}};
		
		return BricknessFactory.newBrickContainer(_network, storagePath);
	}

	private File rootDirectory(String name) {
		String fileName = ".sneer-"+StringUtils.deleteWhitespace(name);
		return new File(_tmpDirectory, fileName);
		
	}

}
