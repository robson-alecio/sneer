package sneer.bricks.pulp.propertystore;

import sneer.foundation.brickness.Brick;

@Brick
public interface PropertyStore {

	void set(String key, String value);

	boolean containsKey(String property);

	String get(String key);

}
