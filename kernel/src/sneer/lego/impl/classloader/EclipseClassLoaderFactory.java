package sneer.lego.impl.classloader;

import java.io.File;
import java.net.URL;

import org.apache.commons.lang.SystemUtils;

import sneer.lego.ClassLoaderFactory;
import sneer.lego.utils.io.BrickImplFilter;
import sneer.lego.utils.io.BrickInterfaceFilter;
import sneer.lego.utils.io.JavaFilter;
import wheel.io.Jars;

public class EclipseClassLoaderFactory implements ClassLoaderFactory {

	private ClassLoader _sneerApi;
	
	private static final ClassLoaderFactory INSTANCE = new EclipseClassLoaderFactory();
	
	private EclipseClassLoaderFactory() {};
	
	public static final ClassLoaderFactory instance() {
		return INSTANCE;
	}
	
	@Override
	public ClassLoader brickClassLoader(String impl, URL ignored) {
		ClassLoader parent = sneerApi();
		File targetDirectory = eclipseTargetDirectory();
		JavaFilter walker = new BrickImplFilter(targetDirectory);
		return createFileClassLoader("brick class loader: "+impl, walker, parent);
	}

	@Override
	public ClassLoader sneerApi() {
		if(_sneerApi != null)
			return _sneerApi;
		
		_sneerApi = eclipseClassLoader();
		//_sneerApi = buildSneerApiForTargetDirectory();
		return _sneerApi;
	}

	private ClassLoader eclipseClassLoader() {
		return this.getClass().getClassLoader();
	}
	
	private ClassLoader buildSneerApiForTargetDirectory() {
		ClassLoader parent = Jars.bootstrapClassLoader();
		File targetDirectory = eclipseTargetDirectory();
		JavaFilter walker = new BrickInterfaceFilter(targetDirectory);
		return createFileClassLoader("api class loader", walker, /* this.getClass().getClassLoader() */ parent);
	}

	private ClassLoader createFileClassLoader(String name, JavaFilter walker, ClassLoader parent) {
		FileClassLoader result = new FileClassLoader(name, walker.listMetaClasses(), parent);
		//result.debug();
		return result;
	}
	
	private File eclipseTargetDirectory() {
		File userDir = SystemUtils.getUserDir();
		File targetDirectory = new File(userDir, "bin");
		return targetDirectory;
	}

//	private URL[] toURLs(Set<File> directories) {
//		URL[] urls = new URL[directories.size()];
//		int i=0;
//		for (File dir : directories) {
//			//System.out.println(dir);
//			urls[i++] = url(dir);
//		}
//		return urls;
//	}
	
	
//	private URL url(File file) {
//		try {
//			return new URL("file://"+file.getPath()+"/");
//		} catch (MalformedURLException e) {
//			throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
//		}
//	}
}
