package functional.adapters;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

import sneer.bricks.config.SneerConfig;
import sneer.bricks.deployer.test.SneerConfigMock;
import sneer.bricks.network.Network;
import sneer.bricks.network.impl.inmemory.InMemoryNetwork;
import functional.SovereignCommunity;
import functional.SovereignParty;

public class SneerCommunity implements SovereignCommunity {

	private int _nextPort = 10000;

	private final Network _network = new InMemoryNetwork();
	
	@Override
	public SovereignParty createParty(String name) {
		SneerConfig config = sneerConfigForParty(name);
		return new SneerParty(name, _nextPort++, _network, config);
	}

	private SneerConfig sneerConfigForParty(String name) {
		File root = rootDirectory(name);
		sneer.lego.utils.FileUtils.cleanDirectory(root);
		SneerConfig config = new SneerConfigMock(root);
		return config;
	}

	private File rootDirectory(String name) {
		File userHome = SystemUtils.getUserHome();
		String fileName = ".sneer+"+StringUtils.deleteWhitespace(name);
		File root = new File(userHome, fileName);
		return root;
	}

	@Override
	public void clearResources(String name) {
		File root = rootDirectory(name);
		sneer.lego.utils.FileUtils.cleanDirectory(root);
	}
}
