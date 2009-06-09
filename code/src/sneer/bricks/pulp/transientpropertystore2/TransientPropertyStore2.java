package sneer.bricks.pulp.transientpropertystore2;

import sneer.foundation.brickness.Brick;

@Brick
public interface TransientPropertyStore2 {

	void set(String key, String value);

	void setSuffix(String suffix);
	String suffix();

}
