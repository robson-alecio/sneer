package sneer.pulp.config.persistence;


import sneer.kernel.container.Brick;

public interface PersistenceConfig extends Brick {

	String persistenceDirectory();

	void setPersistenceDirectory(String directory);
	
}
