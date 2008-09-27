package sneer.pulp.config.persistence;

import java.io.File;

import sneer.kernel.container.Brick;

public interface PersistenceConfig extends Brick {

	File persistenceDirectory();
	
}
