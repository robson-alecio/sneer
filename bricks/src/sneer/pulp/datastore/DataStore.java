package sneer.pulp.datastore;

import sneer.kernel.container.Brick;

public interface DataStore extends Brick {
	
	void set(String key, String value);
	void set(String key, long value);

	boolean containsKey(String property);

	String get(String key);
	long getLong(String key);

}
