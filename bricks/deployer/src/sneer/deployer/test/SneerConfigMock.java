package sneer.deployer.test;

import java.io.File;

import sneer.config.SneerConfig;

public class SneerConfigMock implements SneerConfig {

	@Override
	public File getSneerDirectory() {
		String dir = System.getProperty("user.dir") + File.separator 
		+ "bricks" + File.separator
		+ "deployer" + File.separator
		+ "test-resources" + File.separator
		+ ".sneer" + File.separator;
		return new File(dir);
	}

}
