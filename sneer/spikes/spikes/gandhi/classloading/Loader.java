package spikes.gandhi.classloading;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

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
	 * java -jar classloadingspike.jar /tmp/classloadingspike/jardirectory
	 * 
	 * 
	 */

	public Loader(String externalClasspath) {
		System.out.println("path: " + externalClasspath);
		try {
			List<URL> pathList = classpath(new File(externalClasspath));

			URL[] urls = pathList.toArray(new URL[0]);

			System.out.println("LOADING **********************");
			ObjectInterface app = test1(urls);
			//ObjectInterface app = test2(urls);
			//ObjectInterface app = test3(urls);

			System.out.println("EXECUTING **********************");
			if (app.toLowercase("AeIoU").equals("aeiou"))
				System.out.println("toLowercase ok");
			else
				System.out.println("toLowercase error");

			if (app.plus().execute(3, 4) == 7)
				System.out.println("plus ok");
			else
				System.out.println("plus error");

			if (app.minus().execute(3, 4) == -1)
				System.out.println("minus ok");
			else
				System.out.println("minus error");

			if (app.multiply().execute(3, 4) == 12)
				System.out.println("multiply ok");
			else
				System.out.println("multiply error");

			System.out.println("SUCCESS !!!!!!!!!!!!!!!!!!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unused")
	private ObjectInterface test1(URL[] urls) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException {
		URLClassLoader ucl = new URLClassLoader(urls, ObjectInterface.class.getClassLoader());
		Class<?> clazz = ucl.loadClass("spikes.gandhi.classloading.ObjectImplementation");
		System.out.println("test 1 - loaded by: " + clazz.getClassLoader().getClass().getName());
		ObjectInterface app = (ObjectInterface) clazz.newInstance();
		return app;
	}

	@SuppressWarnings("unused")
	private ObjectInterface test2(URL[] urls) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException {
		URLClassLoader systemClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		Class<?> sysLoaderClass = URLClassLoader.class;
		Method method = sysLoaderClass.getDeclaredMethod("addURL", new Class[] { URL.class });
		method.setAccessible(true);
		for (URL url : urls)
			method.invoke(systemClassLoader, new Object[] { url });
		Class<?> clazz = systemClassLoader.loadClass("spikes.gandhi.classloading.ObjectImplementation");
		System.out.println("test 2 - loaded by: " + clazz.getClassLoader().getClass().getName());
		ObjectInterface app = (ObjectInterface) clazz.newInstance();
		return app;
	}

	@SuppressWarnings("unused")
	private ObjectInterface test3(URL[] urls) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException {
		URLClassLoader ucl = new URLClassLoader(urls, ObjectInterface.class.getClassLoader());
		URLClassLoader systemClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		Class<?> sysLoaderClass = URLClassLoader.class;
		Method method = sysLoaderClass.getDeclaredMethod("addURL", new Class[] { URL.class });
		method.setAccessible(true);
		for (URL url : urls)
			method.invoke(systemClassLoader, new Object[] { url });
		Class<?> clazz = ucl.loadClass("spikes.gandhi.classloading.ObjectImplementation");
		System.out.println("test 3 - loaded by: " + clazz.getClassLoader().getClass().getName());
		ObjectInterface app = (ObjectInterface) clazz.newInstance();
		return app;
	}

	private List<URL> classpath(File classesDirectory) throws MalformedURLException {
		String classpath = System.getProperty("java.class.path");
		String[] paths = classpath.split(File.pathSeparator);
		List<URL> pathList = new ArrayList<URL>();
		pathList.add(classesDirectory.toURI().toURL());
		for (String temp : paths)
			pathList.add((new File(temp)).toURI().toURL());
		return pathList;
	}

	public static void main(String[] args) {
		new Loader(args[0]);
	}

}
