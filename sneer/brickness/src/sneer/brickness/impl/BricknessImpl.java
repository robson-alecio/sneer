package sneer.brickness.impl;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
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
import sneer.commons.lang.ByRef;

public class BricknessImpl implements Brickness {
	
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

	
	private final Environment _environment;
	private final Bindings _bindings;
	private final ClassLoader _apiClassLoader;

	
	public Environment environment() {
		return _environment;
	}

	
	private CachingEnvironment createEnvironment() {
		return new CachingEnvironment(Environments.compose(_bindings.environment(), new Environment(){ @Override public <T> T provide(Class<T> brick) {
			return loadBrick(ClassFiles.classpathRootFor(brick), brick.getName());
		}}));
	}

	
	private <T> T loadBrick(File classRootDirectory, String brickName) {
		try {
			return tryToLoadBrick(classRootDirectory, brickName);
		} catch (Exception e) {
			throw new BrickLoadingException("Exception loading brick: " + brickName + ": " + e.getMessage() + "(src: " + classRootDirectory.getAbsolutePath() + ")", e);
		}
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
		return new ClassLoaderForPackage(classRootDirectory, _apiClassLoader, natures, implPackage);
	}

	
	private List<Nature> naturesFor(Class<?> brick) {
		final Brick annotation = brick.getAnnotation (Brick.class);
		if (annotation == null) throw new BrickLoadingException("Brick '" + brick.getName() + "' is not annotated as such!");

		return naturesImplsFor(annotation.value());
	}
	
	private List<Nature> naturesImplsFor(final Class<? extends Nature>[] natureClasses) {
		final ArrayList<Nature> result = new ArrayList<Nature>(natureClasses.length);
		for (Class<? extends Nature> natureClass : natureClasses)
			result.add(environment().provide(natureClass));
		
		return result;
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

	
	private String implNameFor(final String brickInterfaceName) {
		return BrickConventions.implClassNameFor(brickInterfaceName);
	}


}

