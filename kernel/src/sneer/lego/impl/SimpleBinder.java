package sneer.lego.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.lego.Binder;

public class SimpleBinder implements Binder {

	private final Map<Class<?>, Object> _instances = new HashMap<Class<?>, Object>();
	
	private Class<?> _pendingType;
	
	@Override
	public Object instanceFor(Class<?> type) {
		return _instances.get(type);
	}
	
	public SimpleBinder bind(Class<?> type) {
		_pendingType = type;
		return this;
	}
	
	private void checkHierarchy(Class<?> implementation) {
	    if (!_pendingType.isAssignableFrom(implementation))
	        throw new IllegalArgumentException();
	}

    public Binder toInstance(Object instance) {
        checkHierarchy(instance.getClass());
        _instances.put(_pendingType, instance);
        _pendingType = null;
        return this;
    }

}
