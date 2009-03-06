package sneer.pulp.classpath.impl;

import static sneer.commons.environments.Environments.my;

import java.io.File;

import sneer.brickness.Brick;
import sneer.kernel.container.ContainerConfig;
import sneer.pulp.classpath.Classpath;
import sneer.pulp.classpath.ClasspathFactory;
import wheel.io.Jars;
import wheel.io.codegeneration.ClassUtils;

class ClasspathFactoryImpl implements ClasspathFactory {

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
		if(_sneerApi == null)
			_sneerApi = findSneerApi();
		
		return _sneerApi;		
	}

	private Classpath findSneerApi() {
		
		Classpath result = fromJarFiles(commonsLang(), commonsCollections(), commonsIo());
		
		try {
			/* try to load from sneer.jar */
			Jars.jarGiven(Brick.class);
			throw new sneer.commons.lang.exceptions.NotImplementedYet();	
		} catch(StringIndexOutOfBoundsException e) {
			return result.compose(buildEclipseClasspath());
		}

	}

	private File commonsLang() {
		return Jars.jarGiven(org.apache.commons.lang.ObjectUtils.class);
	}
	private File commonsCollections() {
		return Jars.jarGiven(org.apache.commons.collections.CollectionUtils.class);
	}
	private File commonsIo() {
		return Jars.jarGiven(org.apache.commons.io.FileUtils.class);
	}

	private Classpath buildEclipseClasspath() {
		Classpath result = newClasspath();
		Classpath kernelPlusWheel = new DirectoryBasedClasspath(ClassUtils.rootDirectoryFor(Brick.class));
		Classpath allBrickApis = new FilteredClasspath(new BrickApiFilter(my(ContainerConfig.class).brickRootDirectory()));
		return result.compose(kernelPlusWheel.compose(allBrickApis));
	}

	@Override
	public Classpath fromJarFiles(File... jarFiles) {
		return new JarBasedClasspath(jarFiles);
	}
}

class SimpleClasspath extends JarBasedClasspath {
	
	private static final File RT_JAR = new File(System.getProperty("java.home")+File.separator+"lib"+File.separator+"rt.jar"); 

	SimpleClasspath() {
		super(RT_JAR);
	}
}
