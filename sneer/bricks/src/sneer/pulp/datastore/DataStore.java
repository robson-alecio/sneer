package sneer.pulp.datastore;

import sneer.brickness.Brick;

@Brick
public interface DataStore {
	
	void set(String key, String value);
	void set(String key, long value);

	boolean containsKey(String property);

	String get(String key);
	long getLong(String key);

}
