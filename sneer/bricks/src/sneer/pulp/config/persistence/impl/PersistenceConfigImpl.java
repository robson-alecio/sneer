package sneer.pulp.config.persistence.impl;

import java.io.File;

import sneer.kernel.container.Inject;
import sneer.kernel.container.SneerConfig;
import sneer.pulp.config.persistence.PersistenceConfig;

class PersistenceConfigImpl implements PersistenceConfig {

	@Inject
	private static SneerConfig _sneerConfig;

	@Override
	public File persistenceDirectory() {
		return _sneerConfig.sneerDirectory();
	}

}
