package sneer.pulp.config.persistence.mocks;

import sneer.pulp.config.persistence.PersistenceConfig;
import wheel.io.files.Directory;
import wheel.io.files.impl.tranzient.TransientDirectory;

public class PersistenceConfigMock implements PersistenceConfig {

	private final Directory _persistenceDirectory;

	public PersistenceConfigMock() {
		_persistenceDirectory = new TransientDirectory();
	}
	
	@Override
	public Directory persistenceDirectory() {
		return _persistenceDirectory;
	}

	@Override
	public void setPersistenceDirectory(Directory directory) {
	}
}
