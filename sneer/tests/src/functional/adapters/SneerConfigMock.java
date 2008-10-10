package functional.adapters;

import java.io.File;

import sneer.kernel.container.SneerConfig;

public class SneerConfigMock implements SneerConfig {

	private File _sneerDirectory;
	
	private File _tmpDirectory;

	private File _brickDirectory;
	
	public SneerConfigMock(File root) {
		_sneerDirectory = new File(root, ".sneer");
		_brickDirectory = new File(_sneerDirectory, "bricks");
		_tmpDirectory = new File(_sneerDirectory,"tmp");
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
		//if(!result.exists()) result.mkdirs();
		return result;
	}

}
