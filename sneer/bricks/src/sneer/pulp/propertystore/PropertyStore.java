package sneer.pulp.propertystore;

import sneer.brickness.Brick;

public interface PropertyStore extends Brick {

	void set(String key, String value);

	boolean containsKey(String property);

	String get(String key);

}
