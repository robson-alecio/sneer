package sneer.pulp.config.persistence.mocks;

import java.io.File;

import sneer.pulp.config.persistence.PersistenceConfig;

public class PersistenceConfigMock implements PersistenceConfig {

	private final String _persistenceDirectory;

	public PersistenceConfigMock(File directory) {
		this(directory.getAbsolutePath());
	}

	public PersistenceConfigMock(String directory) {
		_persistenceDirectory = directory;
	}
	
	@Override
	public String persistenceDirectory() {
		return _persistenceDirectory;
	}

	@Override
	public void setPersistenceDirectory(String directory) {
	}
}
