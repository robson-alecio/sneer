package sneer.brickness.impl;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sneer.brickness.Brick;
import sneer.brickness.BrickConventions;
import sneer.brickness.BrickLoadingException;
import sneer.brickness.Brickness;
import sneer.brickness.Nature;
import sneer.brickness.testsupport.ClassFiles;
import sneer.commons.environments.Bindings;
import sneer.commons.environments.CachingEnvironment;
import sneer.commons.environments.Environment;
import sneer.commons.environments.Environments;
import sneer.commons.lang.Producer;

public class BricknessImpl implements Brickness {
	
	private final Environment _environment;
	private final Bindings _bindings;
	private final ClassLoader _apiClassLoader;

	public BricknessImpl(Object... bindings) {
		this(BricknessImpl.class.getClassLoader(), bindings);
	}

	public BricknessImpl(ClassLoader apiClassLoader, Object... bindings) {
		_apiClassLoader = apiClassLoader;
		
		_bindings = new Bindings();
		_bindings.bind(this);
		_bindings.bind(bindings);
		
		_environment = createEnvironment();
	}

	private CachingEnvironment createEnvironment() {
		return new CachingEnvironment(Environments.compose(_bindings.environment(), new Environment(){ @Override public <T> T provide(Class<T> brick) {
			return loadBrick(ClassFiles.classpathRootFor(brick), brick.getName());
		}}));
	}

	private <T> T loadBrick(File classRootDirectory, String brickName) {
		try {
			return tryToLoadBrick(classRootDirectory, brickName);
		} catch (ClassNotFoundException e) {
			throw new BrickLoadingException(e);
		} catch (Exception e) {
			throw new BrickLoadingException("Exception loading brick: " + brickName + ": " + e.getMessage() + "(src: " + classRootDirectory.getAbsolutePath() + ")", e);
		}
	}

	public void placeBrick(File classRootDirectory, String brickName) {
		try {
			tryToPlaceBrick(classRootDirectory, brickName);
		} catch (Exception e) {
			throw new BrickLoadingException("Exception placing brick: " + brickName + ": " + e.getMessage() + "(src: " + classRootDirectory.getAbsolutePath() + ")", e);
		}
	}

	public Environment environment() {
		return _environment;
	}

	private void tryToPlaceBrick(File classRootDirectory, String brickName) throws ClassNotFoundException {
		Class<?> brick = _apiClassLoader.loadClass(brickName);
		ClassLoader classLoader = newImplPackageLoader(classRootDirectory, brick);
		Class<?> brickImpl = classLoader.loadClass(implNameFor(brickName));
		
		_bindings.bind(instantiateInEnvironment(brickImpl));
	}

	private <T> T tryToLoadBrick(File classRootDirectory, String brickName) throws ClassNotFoundException {
		Class<?> brick = _apiClassLoader.loadClass(brickName);
		ClassLoader implLoader = newImplPackageLoader(classRootDirectory, brick);
		Class<?> brickImpl = implLoader.loadClass(implNameFor(brickName));
		
		return (T) instantiateInEnvironment(brickImpl);
	}

	private ClassLoader newImplPackageLoader(File classRootDirectory, Class<?> brick) {
		String implPackage = BrickConventions.implPackageFor(brick.getName());
		List<Nature> natures = naturesFor(brick);
		if (natures.isEmpty())
			return new ClassLoaderForPackage(classRootDirectory, implPackage, _apiClassLoader);
		
		return new ClassLoaderForPackageWithNatures(classRootDirectory, implPackage, _apiClassLoader, natures);
	}

	private List<Nature> naturesFor(Class<?> brick) {
		final Brick annotation = brick.getAnnotation (Brick.class);
		if (annotation == null) {
			throw new BrickLoadingException("Brick '" + brick.getName() + "' is not annotated as such!");
		}
		final Class<? extends Nature>[] natureClasses = annotation.value();
		if (natureClasses.length == 0)
			return Collections.emptyList();
		return naturesFor(natureClasses);
	}

	private List<Nature> naturesFor(final Class<? extends Nature>[] natureClasses) {
		final ArrayList<Nature> result = new ArrayList<Nature>(natureClasses.length);
		for (Class<? extends Nature> natureClass : natureClasses)
			result.add(environment().provide(natureClass));
		
		return result;
	}

	private Object instantiateInEnvironment(final Class<?> brickImpl) {
		return Environments.produceWith(_environment, new Producer<Object>() { @Override public Object produce() {
			try {
				return newInstance(brickImpl);
			} catch (Exception e) {
				throw new BrickLoadingException(e);
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

