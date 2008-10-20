package spikes.klaus.classloading;


public class Main extends ClassLoader {

	public static void main(String[] args) throws Exception {
		ClassLoader cl = new MyClassLoader();
		Class<?> clazz = cl.loadClass("spikes.klaus.classloading.HelloWorld");
		clazz.newInstance();
	}

	
}
