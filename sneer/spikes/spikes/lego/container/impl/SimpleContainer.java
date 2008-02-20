package spikes.lego.container.impl;

import java.util.HashMap;
import java.util.Map;

import spikes.lego.container.Container;
import spikes.lego.container.Injector;
import spikes.legobricks.name.NameKeeper;
import spikes.legobricks.name.impl.NameKeeperImpl;
import spikes.legobricks.store.ObjectStore;
import spikes.legobricks.store.impl.ObjectStoreImpl;
import wheel.lang.Casts;

public class SimpleContainer implements Container {

	private Map<Class<?>, Object> registry = new HashMap<Class<?>, Object>();
	
	private Injector _injector;
	
	public SimpleContainer() {
		_injector = new FieldInjector(this);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T produce(Class<T> clazz) {
		T component = (T) registry.get(clazz);
		if(component != null) return component;
		
		component = lookup(clazz, component);
		inject(component);
		registry.put(clazz, component);
		return component;
	}

	private <T> T lookup(Class<T> clazz, T component) {
		if(ObjectStore.class.equals(clazz)) {
			component = Casts.uncheckedGenericCast(new ObjectStoreImpl());
		} else if(NameKeeper.class.equals(clazz)) {
			component = Casts.uncheckedGenericCast(new NameKeeperImpl());
		}
		return component;
	}

	@Override
	public <T> T create(Class<T> clazz) {
		try {
			T obj = clazz.newInstance();
			inject(obj);
			return obj;
		} catch (Throwable t) {
			//Fix: rethrow this exception
			throw new RuntimeException("Error creating class: "+clazz.getName(), t);
		}
	}

	private void inject(Object component) {
		try {
			_injector.inject(component);
		} catch (Throwable t) {
			//Fix: rethrow this exception
			throw new RuntimeException("Error injecting dependencies on: "+component, t);
		}
	}
	
}
