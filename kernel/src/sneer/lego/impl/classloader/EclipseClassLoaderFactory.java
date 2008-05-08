package sneer.lego.impl.classloader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.SystemUtils;

import sneer.bricks.dependency.Dependency;
import sneer.bricks.dependency.DependencyManager;
import sneer.lego.ClassLoaderFactory;
import sneer.lego.Inject;
import sneer.lego.utils.FileUtils;
import sneer.lego.utils.io.BrickImplFilter;
import sneer.lego.utils.io.JavaFilter;

public class EclipseClassLoaderFactory implements ClassLoaderFactory {

	private JavaFilter _filter;
	
	private Map<Class<?>, ClassLoader> _classLoaderByBrick = new HashMap<Class<?>, ClassLoader>();
	
	@Inject
	private DependencyManager _dependencies;
	
	@Override
	public ClassLoader brickClassLoader(Class<?> brickClass, File brickDirectory) {
		ClassLoader result = _classLoaderByBrick.get(brickClass);
		if(result != null)
			return result;
		
		ClassLoader parent = sneerApi();

		if(FileUtils.isEmpty(brickDirectory)) {
			//useful for eclipse development
			result = fileClassLoader(parent);
			_classLoaderByBrick.put(brickClass, result);
			return result; 
		}
		
		List<Dependency> dependencies = _dependencies.dependenciesFor(brickClass.getName());
		result = new BrickClassLoader(parent, brickClass, brickDirectory, dependencies);
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
}
