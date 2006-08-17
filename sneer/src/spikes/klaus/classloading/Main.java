package spikes.klaus.classloading;

import java.io.RandomAccessFile;

public class Main extends ClassLoader {

	public static void main(String[] args) throws Exception {
//		Class<?> clazz = new URLClassLoader(new URL[] { jar.toURI().toURL() }).loadClass(className);
//		clazz.getMethod("main", new Class[] { String[].class }).invoke(null, new Object[] { args });

//		ClassLoader loader = loader();
//		loader.loadClass("spikes.klaus.classloading.Main");

		new Main().run();

	}

	private void run() throws Exception {
		RandomAccessFile file = new RandomAccessFile("bin/spikes/klaus/classloading/HelloWorld.class", "r");
		byte[] bytecode = new byte[(int)file.length()];
		file.readFully(bytecode);
        Class<?> clazz = defineClass("spikes.klaus.classloading.HelloWorld", bytecode, 0, bytecode.length);
		clazz.getMethod("run", new Class[] {}).invoke(null, new Object[] {});
	}

//	private static ClassLoader loader() {
//		return new ClassLoader() {
//			
//			@Override
//	        public Class<?> findClass(String name) {
////	            byte[] b = loadClassData(name);
////	            return defineClass(name, b, 0, b.length);
//	        	
//	        	System.out.println(name);
//	        	return null;
//	        }
//		};
//	}

}
