package sneer.lego.impl;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sneer.lego.Binder;
import sneer.lego.Configurable;
import sneer.lego.ConfigurationFactory;
import sneer.lego.Container;
import sneer.lego.Crashable;
import sneer.lego.Injector;
import sneer.lego.LegoException;
import sneer.lego.Startable;
import sneer.lego.impl.classloader.BrickClassLoader;
import sneer.lego.utils.ObjectUtils;
import wheel.lang.Threads;

/**
 * This is a dam simple container which will be replaced soon!
 */
public class SimpleContainer implements Container {
	
	private static final Logger log = LoggerFactory.getLogger(SimpleContainer.class);

	private Map<Class<?>, Object> _registry = new HashMap<Class<?>, Object>();
	
	private Injector _injector;
	
	private Binder _binder;
	
	private ConfigurationFactory _configurationFactory;

    public SimpleContainer() {
        this(null);
    }

	public SimpleContainer(Binder binder) {
        this(binder, null);
    }

	public SimpleContainer(Binder binder, ConfigurationFactory configurationFactory) {
		_binder = binder;
		_injector = new FieldInjector(this);
		if(configurationFactory != null) {
		    _configurationFactory = configurationFactory;
		} else {
		    _configurationFactory = findConfigurationFactory();
		}
		log.info("*** SimpleContainer created ***");
	}


	private ConfigurationFactory findConfigurationFactory()
    {
	    try
        {
            ConfigurationFactory factory = lookup(ConfigurationFactory.class);
            if(factory != null) return factory;
        }
        catch (Exception ignored)
        {
            log.info("Can't find ConfigurationFactory. Error message is: {}", ignored.getMessage(), ignored);
        }
        return NullConfigurationFactory.instance();
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
		return component;
	}

	private <T> T instantiate(Class<T> intrface, Object... args) throws LegoException {
		T component;
		try {
			component = lookup(intrface, args);
		} catch (Exception e) {
			throw new LegoException("Error creating: "+intrface.getName(), e);
		}

		inject(component);
		if(component instanceof Configurable) {
		    Configuration config = _configurationFactory.getConfiguration(component.getClass());
		    ((Configurable) component).configure(config);
		}
		if (component instanceof Startable) {
		    try
		    {
		        ((Startable)component).start();
		    }
		    catch (Exception e)
		    {
		        throw new LegoException("Error starting brick: "+intrface.getName(), e);
		    }
		}
		return component;
	}
	
	@SuppressWarnings("unchecked") //Refactor Try to use Casts.unchecked..()
	private <T> T lookup(Class<T> intrface, Object... args) throws Exception {

	    Object result = instanceFor(intrface);
	    if(result != null) return (T) result;

	    String appRoot = getAppRoot();
		String dirName = FilenameUtils.concat(appRoot, intrface.getName()); 
		URL url = new URL("file://"+dirName+"/");
		
		String implementation = implementationFor(intrface); 
		ClassLoader cl = getClassLoader(implementation, url);
		Class impl = cl.loadClass(implementation);
		if(!intrface.isInterface() && args != null && args.length > 0) {
			Constructor c = findConstructor(impl, args);
			boolean before = c.isAccessible();
			c.setAccessible(true);
			result = c.newInstance(args);
			c.setAccessible(before);
		} else {
			result = impl.newInstance();
		}

		log.info("brick {} created", result);
		return (T) result;		

	}

	private Constructor<?> findConstructor(Class<?> clazz, Object... args) throws Exception {
		
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

	private String getAppRoot() {
		//Fix: replace by a Brick
		String appRoot = System.getProperty("user.home") + File.separator + ".sneer" + File.separator + "bricks";
		return appRoot;
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
