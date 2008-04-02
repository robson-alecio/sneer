package sneer.bricks.deployer.test;

import java.io.File;

import sneer.bricks.config.SneerConfig;

public class SneerConfigMock implements SneerConfig {

	@Override
	public File sneerDirectory() {
		String dir = System.getProperty("user.dir") + File.separator 
		+ "bricks" + File.separator
		+ "deployer" + File.separator
		+ "test-resources" + File.separator
		+ ".sneer" + File.separator;
		return new File(dir);
	}

	@Override
	public File brickRootDirectory() {
		throw new wheel.lang.exceptions.NotImplementedYet();
	}

}
