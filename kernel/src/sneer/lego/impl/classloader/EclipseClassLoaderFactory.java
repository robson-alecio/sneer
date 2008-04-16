package sneer.lego.impl.classloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.SystemUtils;

import sneer.lego.ClassLoaderFactory;
import sneer.lego.utils.classloader.FileClassLoader;
import sneer.lego.utils.io.FilteringDirectoryWalker;
import sneer.lego.utils.io.JavaImplDirectoryWalker;
import sneer.lego.utils.io.JavaInterfaceDirectoryWalker;
import wheel.io.Jars;

public class EclipseClassLoaderFactory implements ClassLoaderFactory {

	ClassLoader _sneerApi;
	
	@Override
	public ClassLoader brickClassLoader(String impl, URL ignored) {
		ClassLoader parent = sneerApi();
		File targetDirectory = eclipseTargetDirectory();
		FilteringDirectoryWalker walker = new JavaImplDirectoryWalker(targetDirectory);
		return new FileClassLoader("brick class loader", walker.list(), parent);
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
		FilteringDirectoryWalker walker = new JavaInterfaceDirectoryWalker(targetDirectory);
		return new FileClassLoader("api class loader", walker.list(), parent);
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
