package sneer.lego.impl.classloader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.SystemUtils;

import sneer.lego.ClassLoaderFactory;
import sneer.lego.utils.FileUtils;
import sneer.lego.utils.io.BrickImplFilter;
import sneer.lego.utils.io.JavaFilter;

public class EclipseClassLoaderFactory implements ClassLoaderFactory {

	private JavaFilter _filter;
	
	private Map<Class<?>, ClassLoader> _classLoaderByBrick = new HashMap<Class<?>, ClassLoader>();
	
	@Override
	public ClassLoader brickClassLoader(Class<?> clazz, File brickDirectory) {
		ClassLoader result = _classLoaderByBrick.get(clazz);
		if(result != null)
			return result;
		
		ClassLoader parent = sneerApi();

		if(FileUtils.isEmpty(brickDirectory)) {
			//useful for eclipse development
			result = fileClassLoader(parent);
			_classLoaderByBrick.put(clazz, result);
			return result; 
		}
		
		result = new BrickClassLoader(parent, clazz, brickDirectory);
		_classLoaderByBrick.put(clazz, result);
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
}
