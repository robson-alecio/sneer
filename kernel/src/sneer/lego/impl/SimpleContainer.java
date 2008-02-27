package sneer.lego.impl;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import sneer.lego.BrickClassLoader;
import sneer.lego.Container;
import sneer.lego.LegoException;
import sneer.lego.Startable;
import sneer.lego.utils.ObjectUtils;

/**
 * This is a dam simple container which will be replaced soon!
 */
public class SimpleContainer implements Container {

	private Map<Class<?>, Object> registry = new HashMap<Class<?>, Object>();
	
	private Injector _injector;

	public SimpleContainer() {
		_injector = new FieldInjector(this);
	}


	@SuppressWarnings("unchecked")
	@Override
	public <T> T produce(String className) {
		return (T) produce(ObjectUtils.loadClass(className));
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T produce(Class<T> clazz) {
		T component = (T) registry.get(clazz);
		if(component != null) return component;
		
		if(clazz.isAssignableFrom(Container.class)) {
			return (T) this;
		}
		
		try {
			component = instantiate(clazz);
		} catch (Exception e) {
			throw new LegoException("Error producing: "+clazz.getName(), e);
		}
		
		registry.put(clazz, component);
		return component;
	}

	private <T> T instantiate(Class<T> clazz) throws Exception {
		T component = lookup(clazz);
		inject(component);
		if (component instanceof Startable)
			((Startable)component).start();
		return component;
	}

	@SuppressWarnings("unchecked") //Refactor Try to use Casts.unchecked..()
	private <T> T lookup(Class<T> clazz) throws Exception {

		String appRoot = getAppRoot();
		String dirName = FilenameUtils.concat(appRoot, clazz.getName()); 
		URL url = new URL("file://"+dirName+"/");
		
		String implementation = getImplementation(clazz); 
		BrickClassLoader cl = new BrickClassLoader(implementation, url);
		System.out.println("loading "+implementation+" from "+url);
		Class impl = cl.loadClass(implementation);
		
		return (T) impl.newInstance();
	}

	private String getImplementation(Class<?> clazz) {
		String name = clazz.getName();
		int index = name.lastIndexOf(".");
		return name.substring(0, index) + ".impl" + name.substring(index) + "Impl";
	}

	private String getAppRoot() {
		//Fix: replace by a Brick
		String appRoot = System.getProperty("user.home") + File.separator + ".sneer" + File.separator + "apps";
		return appRoot;
	}

	private void inject(Object component) {
		try {
			_injector.inject(component);
		} catch (Throwable t) {
			throw new LegoException("Error injecting dependencies on: "+component, t);
		}
	}
}
