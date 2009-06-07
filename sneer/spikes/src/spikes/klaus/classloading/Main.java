package spikes.klaus.classloading;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLClassLoader;

public class Main extends ClassLoader {

	public static void main(String[] args) throws Exception {
//		ClassLoader cl = new TracingClassLoader(new File("bin"));
		
		ClassLoader cl = createGarbageCollectableClassLoader(new File("bin"));
		Class<?> clazz = cl.loadClass("spikes.klaus.classloading.HelloWorld");
		clazz.newInstance();
		
		cl = null;
		clazz = null;
		System.gc();
		checkIfResourcesAreFreed();
	}

	private static void checkIfResourcesAreFreed() throws IOException {
		System.out.println("File deleted: " + new File("tmp.tmp").delete());
		new ServerSocket(12345);
	}

	private static URLClassLoader createGarbageCollectableClassLoader(File binDirectory) throws Exception {
		ClassLoader noParent = null;
		return new URLClassLoader(new URL[]{ binDirectory.toURI().toURL() }, noParent);
	}

}


