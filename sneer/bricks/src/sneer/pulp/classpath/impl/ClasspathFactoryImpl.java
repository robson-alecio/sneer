package sneer.pulp.classpath.impl;

import java.io.File;
import java.util.List;

import sneer.kernel.container.Brick;
import sneer.kernel.container.ContainerConfig;
import sneer.kernel.container.Inject;
import sneer.pulp.classpath.Classpath;
import sneer.pulp.classpath.ClasspathFactory;
import wheel.io.Jars;
import wheel.io.codegeneration.ClassUtils;

class ClasspathFactoryImpl implements ClasspathFactory {

	@Inject
	private ContainerConfig _config;
	
	private Classpath _sneerApi;

	@Override
	public Classpath newClasspath() {
		return new SimpleClasspath();
	}

	@Override
	public Classpath fromClassDir(File rootFolder) {
		return new DirectoryBasedClasspath(rootFolder);
	}

	@Override
	public Classpath sneerApi() {
		if(_sneerApi != null) {
			return _sneerApi;
		}
		
		try {
			/* try to load from sneer.jar */
			Jars.jarGiven(Brick.class);
			throw new wheel.lang.exceptions.NotImplementedYet();	
		} catch(StringIndexOutOfBoundsException e) {
			/*  
			 * running from eclipse ?
			 */
			_sneerApi = buildEclipseClasspath();
			return _sneerApi;
		}
	}

	private Classpath buildEclipseClasspath() {
		Classpath cp = newClasspath();
		Classpath kernelPlusWheel = new DirectoryBasedClasspath(ClassUtils.rootDirectoryFor(Brick.class));
		Classpath allBrickApis = new FilteredClasspath(new BrickApiFilter(_config.brickRootDirectory()));
		final Classpath sneerApi = cp.compose(kernelPlusWheel.compose(allBrickApis));
		return sneerApi;
	}

	@Override
	public Classpath fromJarFiles(List<File> jarFiles) {
		return new JarBasedClasspath(jarFiles);
	}
}

class SimpleClasspath extends JarBasedClasspath {
	SimpleClasspath() {
		super(RT_JAR);
	}
}
