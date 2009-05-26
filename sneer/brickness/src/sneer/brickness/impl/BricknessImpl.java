package sneer.brickness.impl;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sneer.brickness.Brick;
import sneer.brickness.BrickConventions;
import sneer.brickness.BrickLayer;
import sneer.brickness.BrickPlacementException;
import sneer.brickness.Brickness;
import sneer.brickness.Nature;
import sneer.commons.environments.Bindings;
import sneer.commons.environments.CachingEnvironment;
import sneer.commons.environments.Environment;
import sneer.commons.environments.Environments;
import sneer.commons.lang.Producer;

public class BricknessImpl implements BrickLayer, Brickness {
	
	private final Environment _environment;
	private final Bindings _bindings;
	private final ApiClassLoader _apiClassLoader = new ApiClassLoader();

	public BricknessImpl(Object... bindings) {
		_bindings = new Bindings();
		_bindings.bind(this);
		_bindings.bind(bindings);
		
		_environment = new CachingEnvironment(_bindings.environment());
	}

	public void placeBrick(File classRootDirectory, String brickName) {
		try {
			tryToPlaceBrick(classRootDirectory, brickName);
		} catch (Exception e) {
			throw new BrickPlacementException("Exception placing brick: " + brickName + ": " + e.getMessage() + "(src: " + classRootDirectory.getAbsolutePath() + ")", e);
		}
	}

	public Environment environment() {
		return _environment;
	}

	private void tryToPlaceBrick(File classRootDirectory, String brickName) throws ClassNotFoundException {
		
		_apiClassLoader.add(classRootDirectory, brickName);
		
		Class<?> brick = _apiClassLoader.loadClass(brickName);
		ClassLoader classLoader = newImplPackageLoader(classRootDirectory, brick);
		Class<?> brickImpl = classLoader.loadClass(implNameFor(brickName));
		
		_bindings.bind(brick, instantiateInEnvironment(brickImpl));
	}

	private ClassLoader newImplPackageLoader(File classRootDirectory, Class<?> brick) {
		String implPackage = BrickConventions.implPackageFor(brick.getName());
		List<Nature> natures = naturesFor(brick);
		if (natures.isEmpty()) {
			return new ClassLoaderForPackage(classRootDirectory, implPackage, _apiClassLoader);
		}
		return new ClassLoaderForPackageWithNatures(classRootDirectory, implPackage, _apiClassLoader, natures);
	}

	private List<Nature> naturesFor(Class<?> brick) {
		final Brick annotation = brick.getAnnotation(Brick.class);
		if (annotation == null) {
			throw new BrickPlacementException("Brick '" + brick.getName() + "' is not annotated as such!");
		}
		final Class<? extends Nature>[] natureClasses = annotation.value();
		if (natureClasses.length == 0)
			return Collections.emptyList();
		return naturesFor(natureClasses);
	}

	private List<Nature> naturesFor(final Class<? extends Nature>[] natureClasses) {
		final ArrayList<Nature> result = new ArrayList<Nature>(natureClasses.length);
		for (Class<? extends Nature> natureClass : natureClasses) {
			final Nature nature = environment().provide(natureClass);
			if (null == nature)
				throw new BrickPlacementException("Implementation for nature '" + natureClass.getName() + "' not found.");
			result.add(nature);
		}
		return result;
	}

	private Object instantiateInEnvironment(final Class<?> brickImpl) {
		return Environments.produceWith(_environment, new Producer<Object>() { @Override public Object produce() {
			try {
				return newInstance(brickImpl);
			} catch (Exception e) {
				throw new BrickPlacementException(e);
			}
		}});
	}

	private Object newInstance(Class<?> brickImpl) throws Exception {
		Constructor<?> constructor = brickImpl.getDeclaredConstructor();
		constructor.setAccessible(true);
		return constructor.newInstance();
	}

	private String implNameFor(final String brickInterfaceName) {
		return BrickConventions.implClassNameFor(brickInterfaceName);
	}


}

