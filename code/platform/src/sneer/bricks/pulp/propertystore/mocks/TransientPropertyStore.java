package sneer.bricks.pulp.propertystore.mocks;

import java.util.HashMap;
import java.util.Map;

import sneer.bricks.pulp.propertystore.PropertyStore;

public class TransientPropertyStore implements PropertyStore {

	private Map<String, String> _properties = new HashMap<String, String>();

	@Override
	public String get(String key) {
		return _properties.get(key);
	}

	@Override
	public void set(String key, String value) {
		_properties.put(key, value);
	}
	
	@Override
	public String toString() {
		return _properties.toString();
	}

	@Override
	public boolean containsKey(String property) {
		return _properties.containsKey(property);
	}

}
