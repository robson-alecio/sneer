package sneer.pulp.transientpropertystore2;

import sneer.brickness.Brick;

@Brick
public interface TransientPropertyStore2 {

	void set(String key, String value);

	void setSuffix(String suffix);
	String suffix();

}
