package sneer.config.impl;

import java.io.File;

import sneer.config.SneerConfig;
import sneer.lego.utils.FileUtils;

public class SneerConfigImpl implements SneerConfig {

	@Override
	public File getSneerDirectory() {
		String userHome = System.getProperty("user.home");
		File home = FileUtils.concat(userHome, ".sneer");
		return home;
	}

}
