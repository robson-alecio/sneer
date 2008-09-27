package sneer.pulp.classpath.impl;

import java.io.File;
import java.util.List;

import sneer.kernel.container.Brick;
import sneer.kernel.container.Inject;
import sneer.kernel.container.SneerConfig;
import sneer.kernel.container.utils.io.BrickApiFilter;
import sneer.pulp.classpath.Classpath;
import sneer.pulp.classpath.ClasspathFactory;
import wheel.io.Jars;

class ClasspathFactoryImpl implements ClasspathFactory {

	@Inject
	private static SneerConfig _config;
	
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

			Classpath cp = newClasspath();
			Classpath kernelPlusWheel = new DirectoryBasedClasspath(_config.eclipseDirectory());
			Classpath allBrickApis = new FilteredClasspath(new BrickApiFilter(_config.brickRootDirectory()));
			_sneerApi = cp.compose(kernelPlusWheel.compose(allBrickApis));
			return _sneerApi;
		}
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
