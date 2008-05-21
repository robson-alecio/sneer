package sneer.lego.impl;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sneer.bricks.config.SneerConfig;
import sneer.bricks.config.impl.SneerConfigImpl;
import sneer.lego.Binder;
import sneer.lego.ClassLoaderFactory;
import sneer.lego.Container;
import sneer.lego.Crashable;
import sneer.lego.Injector;
import sneer.lego.LegoException;
import sneer.lego.Startable;
import sneer.lego.impl.classloader.EclipseClassLoaderFactory;
import sneer.lego.utils.ObjectUtils;

/**
 * This is a dam simple container which will be replaced soon!
 */
public class SimpleContainer implements Container {
	
	private static final Logger log = LoggerFactory.getLogger(SimpleContainer.class);

	private Map<Class<?>, Object> _registry = new HashMap<Class<?>, Object>();
	
	private ClassLoaderFactory _classloaderFactory;
	
	private Injector _injector;
	
	private Binder _binder;
	
	private SneerConfig _sneerConfig;

    public SimpleContainer() {
        this(null);
    }

	public SimpleContainer(Binder binder) {
		_binder = binder;
		_injector = new FieldInjector(this);
		log.info("*** SimpleContainer created ***");
	}



    @SuppressWarnings("unchecked")
	@Override
	public <T> T produce(String className) {
		return (T) produce(ObjectUtils.loadClass(className));
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T produce(Class<T> clazz) {
		T component = (T) _registry.get(clazz);
		if(component != null) return component;

		if(clazz.isAssignableFrom(Container.class)) {
			return (T) this;
		}
		
		if(clazz.isAssignableFrom(Injector.class)) {
			return (T) _injector;
		}
		
		component = instantiate(clazz);
		_registry.put(clazz, component);
		//checkClassLoaders(clazz, component);
		return component;
	}
	
//	@SuppressWarnings("unused")
//	private void checkClassLoaders(Class<?> clazz, Object component) {
//		Class<?>[] interfaces = component.getClass().getInterfaces();
//		ClassLoader parent = clazz.getClassLoader();
//		System.out.println(clazz.getName() + " : "+parent);
//		for (Class<?> intrface : interfaces) {
//			ClassLoader cl = intrface.getClassLoader();
//			System.out.println(intrface.getName() + " : " + intrface.getClassLoader() + (parent == cl ? "*" : ""));
//		}
//	}
	
	private <T> T instantiate(Class<T> intrface, Object... args) throws LegoException {
		T component;
		try {
			component = lookup(intrface, args);
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
	private <T> T lookup(Class<T> clazz, Object... args) throws Exception {

		Object result = instanceFor(clazz);
	    if(result != null) return (T) result;

		String implementation = implementationFor(clazz); 
		File brickDirectory = sneerConfig().brickDirectory(clazz);
		ClassLoader cl = getClassLoader(clazz, brickDirectory);
		Class impl = cl.loadClass(implementation);
		result = construct(impl, args);
		log.info("brick {} created", result);
		return (T) result;		

	}

	private <T> Object construct(Class<?> impl, Object... args) throws Exception {
		Object result;
		Constructor<?> c = findConstructor(impl, args);
		boolean before = c.isAccessible();
		c.setAccessible(true);
		if(args != null && args.length > 0)
			result = c.newInstance(args);
		else
			result = c.newInstance();
		
		c.setAccessible(before);
		
		return result;
	}

	private Constructor<?> findConstructor(Class<?> clazz, Object... args) throws Exception {
		
		if(args == null || args.length == 0)
			return clazz.getDeclaredConstructor();
		
		Class<?>[] argTypes = new Class<?>[args.length];
		for(int i=0 ; i<args.length ; i++) {
			argTypes[i] = args[i].getClass();
		}
		
		Constructor<?>[] constructors = clazz.getDeclaredConstructors();
		for (Constructor<?> constructor : constructors) {
			Class<?>[] parameterTypes = constructor.getParameterTypes();
			if(parameterTypes.length == args.length) {
				parameterTypes = ClassUtils.primitivesToWrappers(parameterTypes);
				if(ClassUtils.isAssignable(argTypes, parameterTypes))
					return constructor;
			}
		}
		throw new Exception("Can't find construtor on "+clazz.getName()+" that matches "+ArrayUtils.toString(argTypes));
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

    private Object instanceFor(Class<?> intrface) {
        if(_binder == null) 
            return null;
        
        return _binder.instanceFor(intrface);
    }

	private String implementationFor(Class<?> intrface) {
		if(_binder != null) {
			String result = _binder.implementationFor(intrface);
			if(result != null) 
				return result;
		}
		
		if(!intrface.isInterface()) {
			return intrface.getName();
		}
		
		String name = intrface.getName();
		int index = name.lastIndexOf(".");
		return name.substring(0, index) + ".impl" + name.substring(index) + "Impl";
	}

	//Fix: check if this code will work on production
	//Hack
	private SneerConfig sneerConfig() {
		if(_sneerConfig != null) 
			return _sneerConfig;
		
		Object result = instanceFor(SneerConfig.class);
		if(result != null) {
			_sneerConfig = (SneerConfig) result;
			return _sneerConfig;
		}
		_sneerConfig = new SneerConfigImpl();
		handleLifecycle(SneerConfig.class, _sneerConfig);
		return _sneerConfig;
	}

	@Override
	public void inject(Object component) {
		try {
			_injector.inject(component);
		} catch (Throwable t) {
			throw new LegoException("Error injecting dependencies on: "+component, t);
		}
	}

	@Override
	public <T> T create(Class<T> clazz) throws LegoException {
		return create(clazz, (Object[]) null);
	}

	@Override
	public <T> T create(Class<T> clazz, Object... args) throws LegoException {
		return instantiate(clazz, args);
	}

	@Override
	public void crash() {
		Set<Class<?>> keys = _registry.keySet();
		for (Class<?> key : keys) {
			Object component = _registry.get(key);
			if(component instanceof Crashable)
				((Crashable) component).crash();
		}
	}
}
