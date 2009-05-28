package sneer.brickness.impl;

import java.lang.reflect.Constructor;

import sneer.brickness.BrickLoadingException;
import sneer.brickness.Brickness;
import sneer.commons.environments.Bindings;
import sneer.commons.environments.CachingEnvironment;
import sneer.commons.environments.Environment;
import sneer.commons.environments.Environments;
import sneer.commons.lang.ByRef;

public class BricknessImpl implements Brickness {
	
	public BricknessImpl(Object... bindings) {
		this(BricknessImpl.class.getClassLoader(), bindings);
	}

	public BricknessImpl(ClassLoader apiClassLoader, Object... bindings) {
		_bindings = new Bindings();
		_bindings.bind(this);
		_bindings.bind(bindings);
		
		_environment = createEnvironment();
		
		_brickImplLoader = new BrickImplLoader(apiClassLoader, _environment);
	}

	
	private final Environment _environment;
	private final Bindings _bindings;
	private final BrickImplLoader _brickImplLoader;

	
	public Environment environment() {
		return _environment;
	}

	
	private CachingEnvironment createEnvironment() {
		return new CachingEnvironment(Environments.compose(_bindings.environment(), new Environment(){ @Override public <T> T provide(Class<T> brick) {
			return loadBrick(brick);
		}}));
	}

	
	private <T> T loadBrick(Class<T> brick) {
		try {
			return tryToLoadBrick(brick);
		} catch (Exception e) {
			throw new BrickLoadingException("Exception loading brick: " + brick + ": " + e.getMessage(), e);
		}
	}

	private <T> T tryToLoadBrick(Class<T> brick) throws ClassNotFoundException {
		Class<?> brickImpl = _brickImplLoader.loadImplClassFor(brick);
		return (T) instantiateInEnvironment(brickImpl);
	}

	private Object instantiateInEnvironment(final Class<?> brickImpl) {
		final ByRef<Object> result = ByRef.newInstance();
		Environments.runWith(_environment, new Runnable() { @Override public void run() {
			result.value = instantiate(brickImpl);
		}});
		return result.value;
	}
	
	private Object instantiate(Class<?> brickImpl) {
		try {
			return tryToInstantiate(brickImpl);
		} catch (Exception e) {
			throw new BrickLoadingException(e);
		}
	}
	
	private Object tryToInstantiate(Class<?> brickImpl)	throws Exception {
		Constructor<?> constructor = brickImpl.getDeclaredConstructor();
		constructor.setAccessible(true);
		return constructor.newInstance();
	}

}

