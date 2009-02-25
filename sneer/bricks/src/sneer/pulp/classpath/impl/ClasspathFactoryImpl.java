package sneer.pulp.classpath.impl;

import static wheel.lang.Environments.my;

import java.io.File;
import java.util.List;

import sneer.kernel.container.Brick;
import sneer.kernel.container.ContainerConfig;
import sneer.pulp.classpath.Classpath;
import sneer.pulp.classpath.ClasspathFactory;
import wheel.io.Jars;
import wheel.io.codegeneration.ClassUtils;

class ClasspathFactoryImpl implements ClasspathFactory {

	private final ContainerConfig _config = my(ContainerConfig.class);
	
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
		if(_sneerApi != null)
			return _sneerApi;
		
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
		Classpath result = newClasspath();
		Classpath kernelPlusWheel = new DirectoryBasedClasspath(ClassUtils.rootDirectoryFor(Brick.class));
		Classpath allBrickApis = new FilteredClasspath(new BrickApiFilter(_config.brickRootDirectory()));
		return result.compose(kernelPlusWheel.compose(allBrickApis));
	}

	@Override
	public Classpath fromJarFiles(List<File> jarFiles) {
		return new JarBasedClasspath(jarFiles);
	}
}

class SimpleClasspath extends JarBasedClasspath {
	
	private static final File RT_JAR = new File(System.getProperty("java.home")+File.separator+"lib"+File.separator+"rt.jar"); 

	SimpleClasspath() {
		super(RT_JAR);
	}
}
