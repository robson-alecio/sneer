package spikes.sneer.kernel.container.impl;

import java.io.File;

import spikes.sneer.kernel.container.SneerConfig;

class SneerConfigImpl implements SneerConfig {

	private File _sneerDirectory;
	
	private File _brickDirectory;

	private File _tmpDirectory;
	
	{
		_sneerDirectory = new File(userHome(), ".sneer");
		_sneerDirectory.mkdirs();
		
		_tmpDirectory = new File(_sneerDirectory, "tmp");
		_tmpDirectory.mkdirs();
		
		_brickDirectory = new File(_sneerDirectory, "bricks");
		_brickDirectory.mkdirs();
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
	public File tmpDirectory() {
		return _tmpDirectory;
	}

	@Override
	public File brickDirectory(Class<?> brickClass) {
		String name = brickClass.getName();
		File result = new File(_brickDirectory, name);
		result.mkdirs();
		return result;
	}

	private static String userHome() {
		String override = System.getProperty("home_override");
		if (override != null) return override;
		
		return System.getProperty("user.home");
	}

}
