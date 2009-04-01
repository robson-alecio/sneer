package sneer.pulp.datastore;

import sneer.brickness.OldBrick;

public interface DataStore extends OldBrick {
	
	void set(String key, String value);
	void set(String key, long value);

	boolean containsKey(String property);

	String get(String key);
	long getLong(String key);

}
