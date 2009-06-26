package sneer.foundation.brickness.impl;

import static sneer.foundation.environments.Environments.my;

import java.lang.reflect.Constructor;

import sneer.foundation.brickness.BrickLoadingException;
import sneer.foundation.environments.Bindings;
import sneer.foundation.environments.CachingEnvironment;
import sneer.foundation.environments.Environment;
import sneer.foundation.environments.EnvironmentUtils;


public class BricknessImpl implements Environment {
	
	public BricknessImpl(Object... bindings) {
		_bindings = new Bindings();
		_bindings.bind(this);
		_bindings.bind(bindings);
	
		_cache = createCachingEnvironment();
		
		_brickImplLoader = new BrickImplLoader();
	}

	
	private final Bindings _bindings;
	private CachingEnvironment _cache;
	private final BrickImplLoader _brickImplLoader;
	private ClassLoader _classLoader;

	
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
		checkClassLoader(brick);
		
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

	private void checkClassLoader(Class<?> brick) {
		if (_classLoader == null)
			_classLoader = brick.getClassLoader();
		
		if (brick.getClassLoader() != _classLoader)
			throw new IllegalStateException("" + brick + " was loaded with " + brick.getClassLoader() + " instead of " + _classLoader + " like previous bricks.");
	}


}

