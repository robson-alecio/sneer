package sneer.brickness.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sneer.brickness.Brick;
import sneer.brickness.BrickConventions;
import sneer.brickness.BrickLoadingException;
import sneer.brickness.Nature;
import sneer.brickness.testsupport.ClassFiles;
import sneer.commons.environments.Environment;

class BrickImplLoader {

	public BrickImplLoader(ClassLoader apiClassLoader, Environment environment) {
		_apiClassLoader = apiClassLoader;
		_environment = environment;
	}

	
	private final ClassLoader _apiClassLoader;
	private final Environment _environment;
	

	Class<?> loadImplClassFor(Class<?> brick) throws ClassNotFoundException {
		ClassLoader classLoader = createClassLoaderForPackage(ClassFiles.classpathRootFor(brick), brick);
		return classLoader.loadClass(implNameFor(brick.getName()));
	}

	private ClassLoader createClassLoaderForPackage(File classRootDirectory, Class<?> brick) {
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
			result.add(_environment.provide(natureClass));
		
		return result;
	}
	
	private static String implNameFor(final String brickInterfaceName) {
		return BrickConventions.implClassNameFor(brickInterfaceName);
	}

}
