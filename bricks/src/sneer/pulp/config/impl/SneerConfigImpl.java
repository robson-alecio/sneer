package sneer.pulp.config.impl;

import java.io.File;

import org.apache.commons.lang.SystemUtils;

import sneer.pulp.config.SneerConfig;

public class SneerConfigImpl implements SneerConfig {

	private File _sneerDirectory;
	
	private File _brickDirectory;

	private File _tmpDirectory;
	
	public SneerConfigImpl() {
		File userHome = SystemUtils.getUserHome();
		
		_sneerDirectory = new File(userHome, ".sneer");
		checkFile(_sneerDirectory);

		_tmpDirectory = new File(_sneerDirectory, "tmp");
		checkFile(_tmpDirectory);
		
		_brickDirectory = new File(_sneerDirectory, "bricks");
		checkFile(_brickDirectory);
	}
	
	
	@Override
	public File sneerDirectory() {
		return _sneerDirectory;
	}

	@Override
	public File brickRootDirectory() {
		return _brickDirectory;
	}

	@Override
	public File eclipseDirectory() {
		return new File(SystemUtils.getUserDir(), "sneerAPI-bin");
	}

	@Override
	public File tmpDirectory() {
		return _tmpDirectory;
	}

	private void checkFile(File file) {
		if(!file.exists()) file.mkdirs();
	}

	@Override
	public File brickDirectory(Class<?> brickClass) {
		String name = brickClass.getName();
		File result = new File(_brickDirectory, name);
		checkFile(result);
		return result;
	}
}
