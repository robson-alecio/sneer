package spikes.lego.impl;

import java.util.HashMap;
import java.util.Map;

import spikes.lego.Container;
import spikes.lego.Startable;
import spikes.legobricks.NameGui;
import spikes.legobricks.name.NameKeeper;
import spikes.legobricks.name.impl.NameKeeperImpl;
import spikes.legobricks.store.ObjectStore;
import spikes.legobricks.store.impl.ObjectStoreImpl;
import spikes.legobricks.threadpool.ThreadPool;
import spikes.legobricks.threadpool.impl.ThreadPoolImpl;

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
		
		component = instantiate(clazz);
		registry.put(clazz, component);
		return component;
	}

	private <T> T instantiate(Class<T> clazz) {
		T component = lookup(clazz);
		inject(component);
		if (component instanceof Startable)
			((Startable)component).start();
		return component;
	}

	@SuppressWarnings("unchecked") //Refactor Try to use Casts.unchecked..()
	private <T> T lookup(Class<T> clazz) {
		if(ObjectStore.class.equals(clazz))
			return (T) new ObjectStoreImpl();
		
		if(NameKeeper.class.equals(clazz))
			return (T) new NameKeeperImpl();

		if(NameGui.class.equals(clazz))
			return (T) new NameGui();

		if(ThreadPool.class.equals(clazz))
			return (T) new ThreadPoolImpl();
			
		throw new IllegalStateException("Could not find " + clazz);
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
