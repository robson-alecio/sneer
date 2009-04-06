package sneer.pulp.transientpropertystore2.impl;

import static sneer.commons.environments.Environments.my;
import sneer.pulp.transientpropertystore.TransientPropertyStore;
import sneer.pulp.transientpropertystore2.TransientPropertyStore2;

public class TransientPropertyStore2Impl implements TransientPropertyStore2 {


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
