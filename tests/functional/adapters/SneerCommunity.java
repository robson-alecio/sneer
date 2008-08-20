package functional.adapters;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import sneer.lego.ContainerUtils;
import sneer.pulp.config.SneerConfig;
import sneer.pulp.network.Network;
import sneer.pulp.network.impl.inmemory.InMemoryNetwork;
import functional.SovereignCommunity;
import functional.SovereignParty;

public class SneerCommunity implements SovereignCommunity {

	private final Network _network = new InMemoryNetwork();
	private int _nextPort = 10000;

	private final File _tmpDirectory = prepareTmpDirectory();
	
	@Override
	public SovereignParty createParty(String name) {
		SneerParty result = produceSneerParty(name);
		
		result.setOwnName(name);
		result.setSneerPort(_nextPort++);
		
		return result;
	}

	private SneerParty produceSneerParty(String name) {
		Object[] bindings = new Object[] {_network, sneerConfigForParty(name)};
	
		return ContainerUtils.newContainer(bindings).produce(SneerParty.class);
	}

	private File prepareTmpDirectory() {
		File tmp = new File("tmp");
		if (tmp.exists())
			tryToClean(tmp);
		else
			tmp.mkdir();
		
		String exclusiveDirectoryName = "test_run_" + System.nanoTime();
		return new File(tmp, exclusiveDirectoryName);
	}

	private void tryToClean(File tmp) {
		try {
			FileUtils.cleanDirectory(tmp);
		} catch (IOException e1) {
			System.gc();
			try {
				FileUtils.cleanDirectory(tmp);
			} catch (IOException e2) {
				System.out.println("Some previous test might be forgetting to close files. " + e2.getMessage());
			}		
		}
	}

	private SneerConfig sneerConfigForParty(String name) {
		File root = rootDirectory(name);
		SneerConfig config = new SneerConfigMock(root);
		return config;
	}

	private File rootDirectory(String name) {
		String fileName = ".sneer-"+StringUtils.deleteWhitespace(name);
		return new File(tmpDirectory(), fileName);
		
	}

	protected File tmpDirectory() {
		return _tmpDirectory;
	}

}
