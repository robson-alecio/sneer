package sneer.bricks.config.impl;

import java.io.File;

import org.apache.commons.lang.SystemUtils;

import sneer.bricks.config.SneerConfig;
import sneer.lego.utils.FileUtils;

public class SneerConfigImpl implements SneerConfig {

	@Override
	public File sneerDirectory() {
		File userHome = SystemUtils.getUserHome();
		File home = FileUtils.concat(userHome, ".sneer");
		return home;
	}

	@Override
	public File brickRootDirectory() {
		File sneerDir = sneerDirectory();
		File brickDir = FileUtils.concat(sneerDir, "bricks");
		return brickDir;
	}

	@Override
	public File eclipseDirectory() {
		return new File(SystemUtils.getUserDir(), "bin");
	}

	@Override
	public File tmpDirectory() {
		return SystemUtils.getJavaIoTmpDir();
	}

}
