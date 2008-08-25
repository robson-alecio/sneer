package sneer.kernel.container.impl.classloader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.SystemUtils;

import sneer.kernel.container.ClassLoaderFactory;
import sneer.kernel.container.utils.FileUtils;
import sneer.kernel.container.utils.io.BrickImplFilter;
import sneer.kernel.container.utils.io.JavaFilter;
import sneer.pulp.config.SneerConfig;

public class EclipseClassLoaderFactory implements ClassLoaderFactory {

	private JavaFilter _filter;
	
	private final Map<Class<?>, ClassLoader> _classLoaderByBrick = new HashMap<Class<?>, ClassLoader>();

	private final SneerConfig _config;
	
	public EclipseClassLoaderFactory(SneerConfig config) {
		_config = config;
	}

	@Override
	public ClassLoader produceBrickClassLoader(Class<?> brickClass, File brickDirectory) {
		ClassLoader result = _classLoaderByBrick.get(brickClass);
		if(result != null)
			return result;
		
		final ClassLoader parent = brickClass.getClassLoader();

		if(FileUtils.isEmpty(brickDirectory)) {
			//useful for eclipse development
			result = fileClassLoader(parent);
			_classLoaderByBrick.put(brickClass, result);
			return result; 
		}
		
		result = new BrickClassLoader(parent, brickClass, brickDirectory);
		_classLoaderByBrick.put(brickClass, result);
		return result;
	}

	//FixUrgent: hack to allow using bricks that are not deployed, but present in your classpath.
	private ClassLoader fileClassLoader(ClassLoader parent) {
		if(_filter == null) {
			File targetDirectory = eclipseTargetDirectory();
			_filter = new BrickImplFilter(targetDirectory);
		}
		return new MetaClassClassLoader(_filter.listMetaClasses(), parent);
	}

	@Override
	public ClassLoader sneerApi() {
		return this.getClass().getClassLoader();
	}
	
	private File eclipseTargetDirectory() {
		File userDir = SystemUtils.getUserDir();
		File targetDirectory = new File(userDir, "bin");
		return targetDirectory;
	}

	@Override
	public ClassLoader newApiClassLoader() {
		return new ApiClassLoader(_config, sneerApi());
	}
}
