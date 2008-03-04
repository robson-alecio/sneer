package sneer.lego.impl;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sneer.lego.Binder;
import sneer.lego.BrickClassLoader;
import sneer.lego.Container;
import sneer.lego.LegoException;
import sneer.lego.Startable;
import sneer.lego.utils.ObjectUtils;
import wheel.lang.Threads;

/**
 * This is a dam simple container which will be replaced soon!
 */
public class SimpleContainer implements Container {
	
	private static final Logger log = LoggerFactory.getLogger(SimpleContainer.class);

	private Map<Class<?>, Object> registry = new HashMap<Class<?>, Object>();
	
	private Injector _injector;
	
	private Binder _binder;

	public SimpleContainer(Binder binder) {
		_binder = binder;
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
		ClassLoader cl = getClassLoader(implementation, url);
		Class impl = cl.loadClass(implementation);
		Object result = impl.newInstance(); 
		return (T) result;
	}

	//FixUrgent: hack to allow using bricks that are not deployed, but present in your classpath. 
	private ClassLoader getClassLoader(String impl, URL url) {
		File file = new File(url.getFile());
		if(!file.exists()) {
			log.info("loading: {} from the System classpath", impl);
			return Threads.contextClassLoader();
		}
		
		log.info("loading: {} from: {}", impl, url);
		return new BrickClassLoader(impl, url);
	}


	private String getImplementation(Class<?> clazz) {
		if(_binder != null) {
			String result = _binder.lookup(clazz);
			if(result != null) 
				return result;
		}
		String name = clazz.getName();
		int index = name.lastIndexOf(".");
		return name.substring(0, index) + ".impl" + name.substring(index) + "Impl";
	}

	private String getAppRoot() {
		//Fix: replace by a Brick
		String appRoot = System.getProperty("user.home") + File.separator + ".sneer" + File.separator + "bricks";
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
