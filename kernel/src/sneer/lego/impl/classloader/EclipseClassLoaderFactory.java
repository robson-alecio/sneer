package sneer.lego.impl.classloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.lang.SystemUtils;

import sneer.lego.ClassLoaderFactory;
import sneer.lego.Inject;
import sneer.lego.Injector;
import sneer.lego.utils.asm.MetaClass;
import sneer.lego.utils.classloader.FileClassLoader;
import sneer.lego.utils.io.ClassInspectorDirectoryWalker;
import sneer.lego.utils.io.FilteringDirectoryWalker;
import sneer.lego.utils.io.JavaImplDirectoryWalker;
import sneer.lego.utils.io.JavaInterfaceDirectoryWalker;
import wheel.io.Jars;

public class EclipseClassLoaderFactory implements ClassLoaderFactory {

	private ClassLoader _sneerApi;
	
	@Inject
	private Injector _injector;
	
	@Override
	public ClassLoader brickClassLoader(String impl, URL ignored) {
		ClassLoader parent = sneerApi();
		File targetDirectory = eclipseTargetDirectory();
		ClassInspectorDirectoryWalker walker = new JavaImplDirectoryWalker(targetDirectory);
		return createFileClassLoader("brick class loader: "+impl, walker, parent);
	}

	@Override
	public ClassLoader sneerApi() {
		if(_sneerApi != null)
			return _sneerApi;
		
		_sneerApi = buildSneerApiForTargetDirectory();
		return _sneerApi;
	}

	private ClassLoader buildSneerApiForTargetDirectory() {
		ClassLoader parent = Jars.bootstrapClassLoader();
		File targetDirectory = eclipseTargetDirectory();
		ClassInspectorDirectoryWalker walker = new JavaInterfaceDirectoryWalker(targetDirectory);
		return createFileClassLoader("api class loader", walker, /* this.getClass().getClassLoader() */ parent);
	}

	private ClassLoader createFileClassLoader(String name, ClassInspectorDirectoryWalker walker, ClassLoader parent) {
		FileClassLoader result = new FileClassLoader(name, walker.listMetaClasses(), parent);
		_injector.inject(result);
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
	
	
	private URL url(File file) {
		try {
			return new URL("file://"+file.getPath()+"/");
		} catch (MalformedURLException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Implement Handle this exception.
		}
	}
}
