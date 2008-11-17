package sneer.pulp.config.persistence.impl;

import java.io.File;

import sneer.pulp.config.persistence.PersistenceConfig;

class PersistenceConfigImpl implements PersistenceConfig {

	private File _directory;

	@Override
	public File persistenceDirectory() {
		return _directory;
	}

	@Override
	public void setPersistenceDirectory(File directory) {
		if (null != _directory)
			throw new IllegalStateException("Persistence directory was already set.");
		_directory = directory;
	}

}
