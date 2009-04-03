package sneer.pulp.transientpropertystore;

import sneer.brickness.OldBrick;

public interface TransientPropertyStore extends OldBrick {

	void set(String key, String value);

	boolean containsKey(String property);

	String get(String key);

}
