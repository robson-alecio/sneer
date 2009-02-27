package sneer.kernel.container.impl;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

import sneer.brickness.Environment;
import sneer.brickness.Environments;
import sneer.kernel.container.Brick;
import sneer.kernel.container.ClassLoaderFactory;
import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerException;
import sneer.kernel.container.SneerConfig;
import sneer.kernel.container.impl.classloader.EclipseClassLoaderFactory;
import sneer.pulp.config.persistence.PersistenceConfig;
import sneer.skin.GuiBrick;
import wheel.lang.Types;

public class ContainerImpl implements Container {
	
	private ClassLoaderFactory _classloaderFactory;
	
	private final SimpleBinder _binder = new SimpleBinder();
	
	private final Environment _environment;
	
	private final SneerConfig _sneerConfig;

	private ClassLoader _apiClassLoader;

	private Set<Class<?>> _interfacesBeingInstantiated = new HashSet<Class<?>>();

	public ContainerImpl(Environment environment, Object... bindings) {
		
		_environment = composeWithBinder(environment);
		
		bindNonGuiBricks(bindings);
		
		_binder.bind(this);
		
		_sneerConfig = produceSneerConfig();
		provide(PersistenceConfig.class).setPersistenceDirectory(persistenceDirectory());
		
		bindGuiBricks(bindings);
	}

	private Environment composeWithBinder(Environment environment) {
		return environment == null
			? _binder
			: Environments.compose(environment, _binder);
	}

	private String persistenceDirectory() {
		return _sneerConfig.sneerDirectory().getAbsolutePath();
	}

	private void bindNonGuiBricks(Object... bindings) {
		for (Object implementation : bindings) {
			if (implementation instanceof GuiBrick)
				continue;
			_binder.bind(implementation);
		}
	}
	
	private void bindGuiBricks(Object... bindings) {
		for (Object implementation : bindings)
			if (implementation instanceof GuiBrick)
				_binder.bind(decorate(implementation));
	}

	@Override
	public Class<? extends Brick> resolve(String brickName) throws ClassNotFoundException {
		if (null == _apiClassLoader) {
			_apiClassLoader = factory().newApiClassLoader();
		}
		return Types.cast(_apiClassLoader.loadClass(brickName));
	}

	@Override
	public <T> T provide(Class<T> type) {
		T result = _environment.provide(type);
		if (result == null) {
			result = decorate(instantiate(type));
			_binder.bind(result);
		}
		return result;
	}
	
	private <T> T decorate(final T component) {
		if (component instanceof GuiBrick)
			return GuiBrickInvocationHandler.decorate(component);
		return component;
	}

	private <T> T instantiate(Class<T> intrface) throws ContainerException {
		if (_interfacesBeingInstantiated.contains(intrface)) throw new Error("Interface " + intrface + " is already being instantiated. This can be caused by circular reference among some of these bricks: " + _interfacesBeingInstantiated);
		_interfacesBeingInstantiated.add(intrface);
		
		try {
			
			return tryToInstantiate(intrface);
			
		} catch (ClassNotFoundException e) {
			System.out.println("Impl for " + intrface + " not found. Please wait while generating...");
			String message = ImplementationGenerator.generateFor(intrface);
			throw new ContainerException(message, e);
		} catch (Exception e) {
			throw new ContainerException(" >>> Exception trying to instantiate " + intrface + ": " + e.getClass().getSimpleName() + " - Message: " + e.getMessage(), e);
		} finally {
			_interfacesBeingInstantiated.remove(intrface);
		}
	}


	private <T> T tryToInstantiate(Class<T> clazz) throws Exception {

		Object result = bindingFor(clazz);
	    if(result != null) return (T)result;

		String implementation = implementationFor(clazz); 
		File brickDirectory = _sneerConfig.brickDirectory(clazz);
		ClassLoader cl = getClassLoader(clazz, brickDirectory);
		Class<?> impl = cl.loadClass(implementation);
		result = construct(impl);
		return (T)result;		

	}

	private <T> Object construct(Class<?> impl) throws Exception {
		Constructor<?> c = impl.getDeclaredConstructor();
		c.setAccessible(true);
		return c.newInstance();
	}

	private ClassLoader getClassLoader(Class<?> brickClass, File brickDirectory) {
		return factory().produceBrickClassLoader(brickClass, brickDirectory);
	}

	private ClassLoaderFactory factory() {
		if(_classloaderFactory == null) {
			_classloaderFactory = new EclipseClassLoaderFactory(_sneerConfig);
		}
		return _classloaderFactory;
	}

    private Object bindingFor(Class<?> type) {
        return _binder.implementationFor(type);
    }

	private String implementationFor(Class<?> type) {
		if(!type.isInterface())
			return type.getName();
		
		return ImplementationGenerator.implementationNameFor(type.getName());
	}

	//Fix: check if this code will work on production
	//Hack
	private SneerConfig produceSneerConfig() {
		Object result = _environment.provide(SneerConfig.class);
		if (result != null) {
			return (SneerConfig) result;
		}
		final SneerConfigImpl newConfig = new SneerConfigImpl();
		_binder.bind(newConfig);
		return newConfig;
	}
}
