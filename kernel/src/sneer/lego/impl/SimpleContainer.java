package sneer.lego.impl;

import java.io.File;
import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sneer.bricks.config.SneerConfig;
import sneer.bricks.config.impl.SneerConfigImpl;
import sneer.lego.ClassLoaderFactory;
import sneer.lego.Container;
import sneer.lego.Injector;
import sneer.lego.LegoException;
import sneer.lego.Startable;
import sneer.lego.impl.classloader.EclipseClassLoaderFactory;
import sneer.lego.utils.ObjectUtils;

public class SimpleContainer implements Container {
	
	private static final Logger log = LoggerFactory.getLogger(SimpleContainer.class);

	private ClassLoaderFactory _classloaderFactory;
	
	private final Injector _injector = new AnnotatedFieldInjector(this);
	
	private final SimpleBinder _binder = new SimpleBinder();
	
	private SneerConfig _sneerConfig;

	public SimpleContainer(Object... bindings) {
		for (Object implementation : bindings)
			_binder.bind(implementation);
		
		_binder.bind(this);
		_binder.bind(_injector);
	}



    @SuppressWarnings("unchecked")
	@Override
	public <T> T produce(String className) {
		return (T) produce(ObjectUtils.loadClass(className));
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T produce(Class<T> type) {
		T result = (T) _binder.implementationFor(type);
		if (result == null) {
			result = instantiate(type);
			_binder.bind(result);
		}
		return result;
	}
	
	private <T> T instantiate(Class<T> intrface) throws LegoException {
		T component;
		try {
			component = lookup(intrface);
		} catch (Exception e) {
			throw new LegoException("Error creating: "+intrface.getName(), e);
		}

		inject(component);
		handleLifecycle(intrface, component);
		return component;
	}

	private <T> void handleLifecycle(Class<T> clazz, T component) {
		if (component instanceof Startable) {
			try {
				((Startable)component).start();
			} catch (Exception e) {
				throw new LegoException("Error starting brick: "+clazz.getName(), e);
			}
		}
	}
	
	@SuppressWarnings("unchecked") //Refactor Try to use Casts.unchecked..()
	private <T> T lookup(Class<T> clazz) throws Exception {

		Object result = bindingFor(clazz);
	    if(result != null) return (T) result;

		String implementation = implementationFor(clazz); 
		File brickDirectory = sneerConfig().brickDirectory(clazz);
		ClassLoader cl = getClassLoader(clazz, brickDirectory);
		Class impl = cl.loadClass(implementation);
		result = construct(impl);
		log.info("brick {} created", result);
		return (T) result;		

	}

	private <T> Object construct(Class<?> impl) throws Exception {
		Constructor<?> c = impl.getDeclaredConstructor();
		boolean before = c.isAccessible();
		c.setAccessible(true);
		try {
			return c.newInstance();
		} finally {
			c.setAccessible(before);
		}
	}

	private ClassLoader getClassLoader(Class<?> brickClass, File brickDirectory) {
		ClassLoader cl = factory().brickClassLoader(brickClass, brickDirectory);
		_injector.inject(cl);
		return cl;
	}

	private ClassLoaderFactory factory() {
		if(_classloaderFactory == null) {
			_classloaderFactory = new EclipseClassLoaderFactory();
		}
		return _classloaderFactory;
	}

    private Object bindingFor(Class<?> type) {
        return _binder.implementationFor(type);
    }

	private String implementationFor(Class<?> type) {
		if(!type.isInterface())
			return type.getName();
		
		String name = type.getName();
		int index = name.lastIndexOf(".");
		return name.substring(0, index) + ".impl" + name.substring(index) + "Impl";
	}

	//Fix: check if this code will work on production
	//Hack
	private SneerConfig sneerConfig() {
		if(_sneerConfig != null) 
			return _sneerConfig;
		
		Object result = bindingFor(SneerConfig.class);
		if(result != null) {
			_sneerConfig = (SneerConfig) result;
			return _sneerConfig;
		}
		_sneerConfig = new SneerConfigImpl();
		handleLifecycle(SneerConfig.class, _sneerConfig);
		return _sneerConfig;
	}

	private void inject(Object component) {
		try {
			_injector.inject(component);
		} catch (Throwable t) {
			throw new LegoException("Error injecting dependencies on: "+component, t);
		}
	}


}
