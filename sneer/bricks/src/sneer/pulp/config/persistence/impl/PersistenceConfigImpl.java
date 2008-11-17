package sneer.pulp.config.persistence.impl;

import sneer.pulp.config.persistence.PersistenceConfig;

class PersistenceConfigImpl implements PersistenceConfig {

	private String _directory;

	@Override
	public String persistenceDirectory() {
		return _directory;
	}

	@Override
	public void setPersistenceDirectory(String directory) {
		if (null != _directory)
			throw new IllegalStateException("Persistence directory was already set.");
		_directory = directory;
	}

}
