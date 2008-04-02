package sneer.bricks.config.impl;

import java.io.File;

import sneer.bricks.config.SneerConfig;
import sneer.lego.utils.FileUtils;

public class SneerConfigImpl implements SneerConfig {

	@Override
	public File sneerDirectory() {
		String userHome = System.getProperty("user.home");
		File home = FileUtils.concat(userHome, ".sneer");
		return home;
	}

	@Override
	public File brickRootDirectory() {
		File sneerDir = sneerDirectory();
		File brickDir = FileUtils.concat(sneerDir, "bricks");
		return brickDir;
	}

}
