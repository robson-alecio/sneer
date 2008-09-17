package sneer.pulp.propertystore;

import sneer.kernel.container.Brick;

public interface PropertyStore extends Brick {

	String get(String key);

	void set(String key, String value);

	boolean containsKey(String property);

}
