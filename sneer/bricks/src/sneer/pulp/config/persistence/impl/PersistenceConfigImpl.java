package sneer.pulp.config.persistence.impl;

import sneer.pulp.config.persistence.PersistenceConfig;
import wheel.io.files.Directory;

class PersistenceConfigImpl implements PersistenceConfig {

	private Directory _directory;

	@Override
	public Directory persistenceDirectory() {
		return _directory;
	}

	@Override
	public void setPersistenceDirectory(Directory directory) {
		if (null != _directory)
			throw new IllegalStateException("Persistence directory was already set.");
		_directory = directory;
	}

}
