package functional.adapters;

import java.io.File;

import org.apache.commons.lang.StringUtils;

import sneer.commons.environments.Environments;
import sneer.kernel.container.Container;
import sneer.kernel.container.Containers;
import sneer.kernel.container.SneerConfig;
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
//		System.out.println("createParty - " + Thread.currentThread());
		final SneerParty party = Environments.wrap(SneerParty.class, newContainer(name));
		party.setOwnName(name);
		party.setSneerPort(_nextPort++);
		return party;
	}

	private Container newContainer(String name) {
		return Containers.newContainer(_network, sneerConfigForParty(name));
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
