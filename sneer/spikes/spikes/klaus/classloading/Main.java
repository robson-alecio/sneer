package spikes.klaus.classloading;

import java.io.RandomAccessFile;
import java.security.Policy;

import spikes.vitor.security.PolicySpike;
import spikes.vitor.security.SpikeProtectionDomain;

public class Main extends ClassLoader {

	public static void main(String[] args) throws Exception {

		new Main().run();

	}

	private void run() throws Exception {
		RandomAccessFile file = new RandomAccessFile("bin/spikes/klaus/classloading/HelloWorld.class", "r");
		byte[] bytecode = new byte[(int)file.length()];
		file.readFully(bytecode);
        Class<?> clazz = defineClass("spikes.klaus.classloading.HelloWorld", bytecode, 0, bytecode.length, new SpikeProtectionDomain());
        
        Policy.setPolicy(new PolicySpike());
        System.setSecurityManager(new SecurityManager());
		clazz.newInstance();
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
