package sneer.pulp.config.persistence;


import sneer.brickness.Brick;

public interface PersistenceConfig extends Brick {

	String persistenceDirectory();

	void setPersistenceDirectory(String directory);
	
}
