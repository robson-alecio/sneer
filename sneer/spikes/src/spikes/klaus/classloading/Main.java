package spikes.klaus.classloading;

import java.net.URL;
import java.security.Policy;

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

	
	private static Object newHello(String url) throws Exception {
		System.out.println(url);
		ClassLoader cl = new MyClassLoader(new URL[]{new URL(url)});
		Class<?> clazz = cl.loadClass("spikes.klaus.classloading.HelloWorld");
		return clazz.newInstance();
	}

	
	

}
