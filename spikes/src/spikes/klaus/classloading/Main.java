package spikes.klaus.classloading;

import java.net.URL;
import java.security.Policy;

import sneer.lego.BrickClassLoader;
import spikes.vitor.security.PolicySpike;

public class Main extends ClassLoader {

	public static void main(String[] args) throws Exception {

		Policy.setPolicy(new PolicySpike());
		System.setSecurityManager(new SecurityManager());

		Object h1 = newHello("file:///home/leandro/.sneer/apps/hello1/");
		System.out.println(h1.getClass().getClassLoader());
		
		Object h2 = newHello("file:///home/leandro/.sneer/apps/hello2/");
		System.out.println(h2.getClass().getClassLoader());

	}

//	private void run() throws Exception {
//		RandomAccessFile file = new RandomAccessFile("bin/spikes/klaus/classloading/HelloWorld.class", "r");
//		byte[] bytecode = new byte[(int)file.length()];
//		file.readFully(bytecode);
//        Class<?> clazz = defineClass("spikes.klaus.classloading.HelloWorld", bytecode, 0, bytecode.length, new SpikeProtectionDomain());
//        
//        Policy.setPolicy(new PolicySpike());
//        System.setSecurityManager(new SecurityManager());
//		clazz.newInstance();
//	}

	
	
	private static Object newHello(String url) throws Exception {
		System.out.println(url);
		ClassLoader cl = new BrickClassLoader(new URL[]{new URL(url)});
		Class<?> clazz = cl.loadClass("spikes.klaus.classloading.HelloWorld");
		return clazz.newInstance();
	}

	
	

}
