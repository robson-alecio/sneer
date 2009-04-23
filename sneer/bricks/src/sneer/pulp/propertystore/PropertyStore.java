package sneer.pulp.propertystore;

import sneer.brickness.Brick;

@Brick
public interface PropertyStore {

	void set(String key, String value);

	boolean containsKey(String property);

	String get(String key);

}
