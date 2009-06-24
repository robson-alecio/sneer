package sneer.bricks.software.code.compilers.classpath.impl;

import static sneer.foundation.environments.Environments.my;

import java.io.File;

import sneer.bricks.software.code.classutils.ClassUtils;
import sneer.bricks.software.code.compilers.classpath.Classpath;
import sneer.bricks.software.code.compilers.classpath.ClasspathFactory;
import sneer.foundation.brickness.Brick;

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
//		Classpath result = fromJarFiles(commonsLang(), commonsCollections(), commonsIo());
		Classpath result = fromJarFiles(); //Refactor: Commons jar files are no longer used. 

		//try {
			/* try to load from sneer.jar */
			//Jars.jarGiven(Brick.class);
			//throw new sneer.commons.lang.exceptions.NotImplementedYet();	

		//} catch(StringIndexOutOfBoundsException e) {
			return result.compose(buildEclipseClasspath());
		//}
	}

	private Classpath buildEclipseClasspath() {
		Classpath kernelPlusWheel = new DirectoryBasedClasspath(my(ClassUtils.class).classpathRootFor(Brick.class));
		return kernelPlusWheel;
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
