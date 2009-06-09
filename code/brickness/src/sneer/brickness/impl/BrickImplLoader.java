package sneer.brickness.impl;

import static sneer.commons.environments.Environments.my;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sneer.brickness.Brick;
import sneer.brickness.BrickConventions;
import sneer.brickness.BrickLoadingException;
import sneer.brickness.Nature;
import sneer.brickness.testsupport.ClassFiles;


class BrickImplLoader {

	public BrickImplLoader(ClassLoader apiClassLoader) {
		_apiClassLoader = apiClassLoader;
	}

	
	private final ClassLoader _apiClassLoader;
	

	Class<?> loadImplClassFor(Class<?> brick) throws ClassNotFoundException {
		File path = ClassFiles.classpathRootFor(brick);
		String implPackage = BrickConventions.implPackageFor(brick.getName());
		List<Nature> natures = naturesFor(brick);

		ClassLoader libsClassLoader = ClassLoaderForBrickLibs.newInstanceIfNecessary(path, implPackage, natures, _apiClassLoader);
		ClassLoader nextClassLoader = libsClassLoader == null ? _apiClassLoader : libsClassLoader;
		ClassLoader packageLoader = new ClassLoaderForPackage(path, implPackage, natures, nextClassLoader);

		return packageLoader.loadClass(implNameFor(brick.getName()));
	}

	
	private List<Nature> naturesFor(Class<?> brick) {
		final Brick annotation = brick.getAnnotation (Brick.class);
		if (annotation == null) throw new BrickLoadingException("Brick '" + brick.getName() + "' is not annotated as such!");

		return naturesImplsFor(annotation.value());
	}
	
	private List<Nature> naturesImplsFor(final Class<? extends Nature>[] natureClasses) {
		final ArrayList<Nature> result = new ArrayList<Nature>(natureClasses.length);
		for (Class<? extends Nature> natureClass : natureClasses)
			result.add(my(natureClass));
		
		return result;
	}
	
	private static String implNameFor(final String brickInterfaceName) {
		return BrickConventions.implClassNameFor(brickInterfaceName);
	}

}
