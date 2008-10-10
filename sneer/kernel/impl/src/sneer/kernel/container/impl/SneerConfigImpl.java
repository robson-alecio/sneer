package sneer.kernel.container.impl;

import java.io.File;

import sneer.kernel.container.SneerConfig;

public class SneerConfigImpl implements SneerConfig {

	private File _sneerDirectory;
	
	private File _brickDirectory;

	private File _tmpDirectory;
	
	public SneerConfigImpl() {
		this(new File(userHome(), ".sneer"));
	}

	public SneerConfigImpl(File sneerDirectory) {
		_sneerDirectory = sneerDirectory;
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

	private static String userHome() {
		String override = System.getProperty("home_override");
		if (override != null) return override;
		
		return System.getProperty("user.home");
	}

}
