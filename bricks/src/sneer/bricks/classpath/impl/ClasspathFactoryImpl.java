package sneer.bricks.classpath.impl;

import java.io.File;

import sneer.bricks.classpath.Classpath;
import sneer.bricks.classpath.ClasspathFactory;
import sneer.bricks.config.SneerConfig;
import sneer.lego.Brick;
import sneer.lego.Inject;
import wheel.io.Jars;

public class ClasspathFactoryImpl implements ClasspathFactory {

	@Inject
	private SneerConfig _config;
	
	private Classpath _sneerApi;
	
	@Override
	public Classpath fromLibDir(File folder) {
		return new LibdirClasspath(folder);
	}

	@Override
	public Classpath newClasspath() {
		return new SimpleClasspath();
	}

	@Override
	public Classpath fromDirectory(File rootFolder) {
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
			_sneerApi = new DirectoryBasedClasspath(_config.eclipseDirectory()); 
			return _sneerApi;
		}
	}

}

class SimpleClasspath extends JarBasedClasspath {
	SimpleClasspath() {
		_elements.add(RT_JAR);
	}
}
