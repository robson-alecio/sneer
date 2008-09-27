package sneer.pulp.datastore.impl;

import sneer.kernel.container.Inject;
import sneer.pulp.datastore.DataStore;
import sneer.pulp.propertystore.PropertyStore;

class DataStoreImpl implements DataStore {

	@Inject
	static private PropertyStore _delegate;


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