package sneer.lego.impl;

import java.util.HashMap;
import java.util.Map;

import sneer.lego.Binder;

public class SimpleBinder implements Binder {

	private final Map<Class<?>, Class<?>> _map = new HashMap<Class<?>, Class<?>>();
	
	private Class<?> _pendingInterface;
	
	@Override
	public Binder bind(Class<?> intrface) {
		_pendingInterface = intrface;
		return this;
	}

	@Override
	public Binder to(Class<?> implementation) {
		if (!_pendingInterface.isAssignableFrom(implementation))
			throw new IllegalArgumentException();

		_map.put(_pendingInterface, implementation);
		_pendingInterface = null;
		return this;
	}

	@Override
	public String implementationFor(Class<?> clazz) {
		Class<?> impl = _map.get(clazz);
		return impl != null ? impl.getName() : null;
	}

}
