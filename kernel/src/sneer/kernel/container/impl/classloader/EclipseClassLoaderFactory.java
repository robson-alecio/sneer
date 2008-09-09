package sneer.kernel.container.impl.classloader;

import java.io.File;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.lang.SystemUtils;

import sneer.kernel.container.ClassLoaderFactory;
import sneer.kernel.container.SneerConfig;
import sneer.kernel.container.utils.FileUtils;
import sneer.kernel.container.utils.io.BrickImplFilter;
import sneer.kernel.container.utils.io.JavaFilter;

public class EclipseClassLoaderFactory implements ClassLoaderFactory {

	private JavaFilter _filter;
	
	private final Map<Class<?>, Reference<ClassLoader>> _classLoaderByBrick = new WeakHashMap<Class<?>, Reference<ClassLoader>>();

	private final SneerConfig _config;
	
	public EclipseClassLoaderFactory(SneerConfig config) {
		_config = config;
	}

	@Override
	public ClassLoader produceBrickClassLoader(Class<?> brickClass, File brickDirectory) {
		final Reference<ClassLoader> cachedRef = _classLoaderByBrick.get(brickClass);
		if(cachedRef != null) {
			final ClassLoader cached = cachedRef.get();
			if (cached != null)
				return cached;
		}
		
		final ClassLoader cl = newBrickClassLoader(brickClass, brickDirectory);
		_classLoaderByBrick.put(brickClass, new WeakReference<ClassLoader>(cl));
		return cl;
	}

	private ClassLoader newBrickClassLoader(Class<?> brickClass, File brickDirectory) {
		final ClassLoader parent = brickClass.getClassLoader();

		if(FileUtils.isEmpty(brickDirectory)) {
			//useful for eclipse development
			return fileClassLoader(parent);
		}
		return new BrickClassLoader(parent, brickClass, brickDirectory);
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
		return getClass().getClassLoader();
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
