package sneer.kernel.container.impl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;

import sneer.kernel.container.Brick;
import sneer.kernel.container.ClassLoaderFactory;
import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerException;
import sneer.kernel.container.Injector;
import sneer.kernel.container.SneerConfig;
import sneer.kernel.container.impl.classloader.EclipseClassLoaderFactory;
import sneer.pulp.config.persistence.PersistenceConfig;
import sneer.skin.GuiBrick;
import wheel.io.files.impl.DurableDirectory;
import wheel.lang.Types;

public class ContainerImpl implements Container {
	
	private ClassLoaderFactory _classloaderFactory;
	
	private final Injector _injector = new AnnotatedFieldInjector(this);
	
	private final SimpleBinder _binder = new SimpleBinder();
	
	private final SneerConfig _sneerConfig;

	private ClassLoader _apiClassLoader;

	public ContainerImpl(Object... bindings) throws IOException {
		bindNonGuiBricks(bindings);
		
		_binder.bind(this);
		_binder.bind(_injector);
		
		_sneerConfig = produceSneerConfig();
		produce(PersistenceConfig.class).setPersistenceDirectory(persistenceDirectory());
		
		bindGuiBricks(bindings);
	}

	private DurableDirectory persistenceDirectory() throws IOException {
		DurableDirectory result = new DurableDirectory(_sneerConfig.sneerDirectory().getAbsolutePath());
		result.mkdirs();
		return result;
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
	public <T> T produce(Class<T> type) {
		T result = (T)_binder.implementationFor(type);
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
		T component;
		try {
			component = lookup(intrface);
		} catch (ClassNotFoundException e) {
			System.out.println("Impl for " + intrface + " not found. Please wait while generating...");
			String message = ImplementationGenerator.generateFor(intrface);
			throw new ContainerException(message, e);
		} catch (Exception e) {
			throw new ContainerException(" >>>" + e.getClass().getSimpleName() + ": " + e.getMessage(), e);
		}

		inject(component);
		return component;
	}


	private <T> T lookup(Class<T> clazz) throws Exception {

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
		ClassLoader result = factory().produceBrickClassLoader(brickClass, brickDirectory);
		inject(result);
		return result;
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
		Object result = bindingFor(SneerConfig.class);
		if (result != null) {
			return (SneerConfig) result;
		}
		final SneerConfigImpl newConfig = new SneerConfigImpl();
		_binder.bind(newConfig);
		return newConfig;
	}

	private void inject(Object component) {
		try {
			_injector.inject(component);
		} catch (Throwable t) {
			throw new ContainerException("Error injecting dependencies on: "+component, t);
		}
	}
}
