package functional.adapters;

import java.io.File;

import org.apache.commons.lang.StringUtils;

import sneer.kernel.container.ContainerUtils;
import sneer.kernel.container.SneerConfig;
import sneer.pulp.network.Network;
import sneer.pulp.network.impl.inmemory.InMemoryNetwork;
import functional.SovereignCommunity;
import functional.SovereignParty;

public class SneerCommunity implements SovereignCommunity {

	private final Network _network = new InMemoryNetwork();
	private int _nextPort = 10000;

	private final File _tmpDirectory;
	
	public SneerCommunity(File tmpDirectory) {
		_tmpDirectory = tmpDirectory;
	}
	
	
	@Override
	public SovereignParty createParty(String name) {
		System.out.println("createParty - " + Thread.currentThread());
		SneerParty result = produceSneerParty(name);
		
		result.setOwnName(name);
		result.setSneerPort(_nextPort++);
		
		return result;
	}

	private SneerParty produceSneerParty(String name) {
		Object[] bindings = new Object[] {_network, sneerConfigForParty(name)};
	
		return ContainerUtils.newContainer(bindings).provide(SneerParty.class);
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
