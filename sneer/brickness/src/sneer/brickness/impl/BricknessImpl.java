package sneer.brickness.impl;

import static sneer.commons.environments.Environments.my;

import java.lang.reflect.Constructor;

import sneer.brickness.BrickLoadingException;
import sneer.commons.environments.Bindings;
import sneer.commons.environments.CachingEnvironment;
import sneer.commons.environments.Environment;
import sneer.commons.environments.EnvironmentUtils;


public class BricknessImpl implements Environment {
	
	public BricknessImpl(Object... bindings) {
		this(BricknessImpl.class.getClassLoader(), bindings);
	}

	public BricknessImpl(ClassLoader apiClassLoader, Object... bindings) {
		_bindings = new Bindings();
		_bindings.bind(this);
		_bindings.bind(bindings);
	
		_cache = createCachingEnvironment();
		
		_brickImplLoader = new BrickImplLoader(apiClassLoader);
	}

	
	private final Bindings _bindings;
	private CachingEnvironment _cache;
	private final BrickImplLoader _brickImplLoader;

	
	@Override
	public <T> T provide(Class<T> intrface) {
		if (my(Environment.class) == null) throw new IllegalStateException("provide() cannot be called outside an environment."); //Delete this line after July 2009 if the exception is never thrown.
		
		return _cache.provide(intrface);
	}

	
	private CachingEnvironment createCachingEnvironment() {
		return new CachingEnvironment(EnvironmentUtils.compose(_bindings.environment(), new Environment(){ @Override public <T> T provide(Class<T> brick) {
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
		return (T) instantiate(brickImpl);
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

