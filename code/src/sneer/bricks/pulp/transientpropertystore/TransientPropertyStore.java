package sneer.bricks.pulp.transientpropertystore;

import sneer.foundation.brickness.Brick;

@Brick
public interface TransientPropertyStore {

	void set(String key, String value);

	boolean containsKey(String property);

	String get(String key);

}
