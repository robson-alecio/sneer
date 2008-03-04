package sneer.lego.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import sneer.lego.Binder;

public class SimpleBinder implements Binder {

	private Map<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>();
	
	@Override
	public Binder bind(Class<?> clazz) {
		map.put(clazz, Object.class);
		return this;
	}

	@Override
	public Binder to(Class<?> clazz) {
		Set<Class<?>> keys = map.keySet();
		for (Class<?> key : keys) {
			if(key.isAssignableFrom(clazz))
				map.put(key, clazz);
		}
		return this;
	}

	@Override
	public String lookup(Class<?> clazz) {
		Class<?> impl = map.get(clazz);
		return impl != null ? impl.getName() : null;
	}

}
