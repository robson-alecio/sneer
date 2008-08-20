package spikes.sandro.gc;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.commons.io.FileUtils;

public class Loader {

	public static void main(String[] args) throws Exception {

		 URL url = Loader.class.getResource("foo.jar");
		 File srcFile = new File(url.getPath());
		 File destFile = new File(srcFile.getParentFile(),"tmp/foo2.jar");
		 FileUtils.deleteDirectory(destFile.getParentFile());
		 destFile.getParentFile().mkdirs();
		 FileUtils.copyFile(srcFile, destFile);
//		 URLClassLoader cl = Jars.createGarbageCollectableClassLoader(destFile.toURI().toURL());
		 URLClassLoader cl = new URLClassLoader(new URL[]{destFile.toURI().toURL()});
		 cl.loadClass("spikes.sandro.gc.FooClass");
		 System.gc();
		 try {
			FileUtils.deleteDirectory(destFile.getParentFile());
			throw new IllegalStateException();
		} catch (Exception expected) {}

		 cl = null;
		 System.gc();
		 FileUtils.deleteDirectory(destFile.getParentFile());
	}
}
