package sneer.bricks.pulp.datastore.impl;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.pulp.datastore.DataStore;
import sneer.bricks.pulp.propertystore.PropertyStore;

class DataStoreImpl implements DataStore {

	private final PropertyStore _delegate = my(PropertyStore.class);


	@Override
	public String get(String key) {
		return _delegate.get(key);
	}
	
	@Override
	public boolean containsKey(String key) {
		return _delegate.containsKey(key);
	}
	
	@Override
	public void set(String key, String value) {
		_delegate.set(key, value);
	}
	

	@Override
	public long getLong(String key) {
		return containsKey(key)
			? Long.parseLong(get(key))
			: 0;
	}

	@Override
	public void set(String key, long value) {
		set(key, "" + value);
	}

}
