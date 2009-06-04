package spikes.sneer.kernel.container.impl.classloader;

import static sneer.commons.environments.Environments.my;

import java.io.File;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

import sneer.hardware.io.file.utils.FileUtils;
import sneer.pulp.clock.Clock;
import sneer.software.code.classutils.ClassUtils;
import sneer.software.code.filefilters.java.JavaFilter;
import spikes.sneer.kernel.container.ClassLoaderFactory;
import spikes.sneer.kernel.container.SneerConfig;
import spikes.sneer.kernel.container.utils.io.BrickImplFilter;

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

		if(my(FileUtils.class).isEmpty(brickDirectory)) {
			//useful for eclipse development
			return fileClassLoader(parent);
		}
		return new OldBrickClassLoader(parent, brickClass, brickDirectory);
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
		return my(ClassUtils.class).rootDirectoryFor(Clock.class);
	}

	@Override
	public ClassLoader newApiClassLoader() {
		return new OldApiClassLoader(_config, sneerApi());
	}
}
