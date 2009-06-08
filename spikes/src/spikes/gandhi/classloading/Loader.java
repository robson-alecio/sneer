package spikes.gandhi.classloading;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class Loader {

	/*
	 * build.xml will generate two directories:
	 * - one with one jar with Loader.class, ObjectInterface.class, and InnerClassInterface.class
	 * - other with ObjectImplementation.class.
	 * 
	 * the loader will be executed from commandline and the path to ObjectImplementation.class will be passed as argument
	 * 
	 * 
	 * Use:
	 * java -jar /tmp/classloadingspike/jardirectory/classloadingspike.jar /tmp/classloadingspike/jardirectory
	 * 
	 * 
	 */

	public Loader(String externalClasspath) throws Exception {
		printMemoryUse();

		ObjectInterface app = loadApp(externalClasspath);
		testBehavior(app);
		printMemoryUse();

		ObjectInterface app2 = loadApp(externalClasspath + "/2");
		testBehavior(app2);
		printMemoryUse();
		
		app = null;
		app2 = null;
		System.gc();
		printMemoryUse();
		
		System.out.println("SUCCESS ! ! !");
	}

	private void printMemoryUse() {
		System.out.println(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
	}

	private ObjectInterface loadApp(String externalClasspath) throws Exception {
		URL[] urls = new URL[]{new File(externalClasspath).toURI().toURL()};
		URLClassLoader ucl = new URLClassLoader(urls, ObjectInterface.class.getClassLoader());
		Class<?> clazz = ucl.loadClass("spikes.gandhi.classloading.ObjectImplementation");
		return (ObjectInterface) clazz.newInstance();
	}

	private void testBehavior(ObjectInterface app) {
		assertEquals("aeiou", app.toLowercase("AeIoU"));
		assertEquals(7, app.plus().execute(3, 4));
		assertEquals(-1, app.minus().execute(3, 4));
		assertEquals(12, app.multiply().execute(3, 4));
	}
	
	private void assertEquals(Object expected, Object observed) {
		if (!observed.equals(expected)) throw new IllegalStateException();
	}

	public static void main(String[] args) throws Exception {
		new Loader(args[0]);
	}

}
