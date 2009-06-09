package sneer.pulp.transientpropertystore;

import sneer.brickness.Brick;

@Brick
public interface TransientPropertyStore {

	void set(String key, String value);

	boolean containsKey(String property);

	String get(String key);

}
