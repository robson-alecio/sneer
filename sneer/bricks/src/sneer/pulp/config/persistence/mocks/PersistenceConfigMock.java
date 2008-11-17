package sneer.pulp.config.persistence.mocks;

import java.io.File;

import sneer.pulp.config.persistence.PersistenceConfig;

public class PersistenceConfigMock implements PersistenceConfig {

	private final File _persistenceDirectory;

	public PersistenceConfigMock(File directory) {
		_persistenceDirectory = directory;
	}
	
	@Override
	public File persistenceDirectory() {
		return _persistenceDirectory;
	}

	@Override
	public void setPersistenceDirectory(File directory) {
	}
}
