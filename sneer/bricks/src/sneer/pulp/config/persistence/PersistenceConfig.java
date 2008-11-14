package sneer.pulp.config.persistence;


import sneer.kernel.container.Brick;
import wheel.io.files.Directory;

public interface PersistenceConfig extends Brick {

	Directory persistenceDirectory();

	void setPersistenceDirectory(Directory directory);
	
}
