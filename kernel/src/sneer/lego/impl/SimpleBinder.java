package sneer.lego.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.lego.Binder;

public class SimpleBinder implements Binder {

	private final Map<Class<?>, Class<?>> _implementations = new HashMap<Class<?>, Class<?>>();
	
	private final Map<Class<?>, Object> _instances = new HashMap<Class<?>, Object>();
	
	private Class<?> _pendingInterface;
	
	@Override
	public Binder bind(Class<?> intrface) {
		_pendingInterface = intrface;
		return this;
	}

	private void checkHierarchy(Class<?> implementation) {
	    if (!_pendingInterface.isAssignableFrom(implementation))
	        throw new IllegalArgumentException();
	}

	@Override
	public Binder to(Class<?> implementation) {
	    checkHierarchy(implementation);
		_implementations.put(_pendingInterface, implementation);
		_pendingInterface = null;
		return this;
	}

    @Override
    public Binder toInstance(Object instance) {
        checkHierarchy(instance.getClass());
        _instances.put(_pendingInterface, instance);
        _pendingInterface = null;
        return this;
    }

	@Override
	public String implementationFor(Class<?> clazz) {
		Class<?> impl = _implementations.get(clazz);
		return impl != null ? impl.getName() : null;
	}

    @Override
    public Object instanceFor(Class<?> intrface) {
        return _instances.get(intrface);
    }

}
