package sneer.bricks.pulp.transientpropertystore2.impl;

import static sneer.foundation.commons.environments.Environments.my;
import sneer.bricks.pulp.transientpropertystore.TransientPropertyStore;
import sneer.bricks.pulp.transientpropertystore2.TransientPropertyStore2;

public class TransientPropertyStore2Impl implements TransientPropertyStore2 {

	{
		my(TransientPropertyStore.class).set("anything", "anything");
	}

	private String _suffix;

	@Override
	public void set(String key, String value) {
		my(TransientPropertyStore.class).set(key, value + _suffix);
	}

	@Override
	public void setSuffix(String suffix) {
		_suffix = suffix;
	}

	@Override
	public String suffix() {
		return _suffix;
	}

}
