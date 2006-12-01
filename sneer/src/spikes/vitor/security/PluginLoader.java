package spikes.vitor.security;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class PluginLoader {
	
	String plugin;
	Class<?> clazz;
	ThreadGroup group;
	
	public PluginLoader(ThreadGroup group, String plugin) {
		super();
		this.plugin = plugin;
		
		ClassLoader loader = createLoader();
		clazz = loadPlugin(loader);
	}
	
	public URL binaryDirectory() {
		try {
			return new File("bin").toURL();
		} catch (MalformedURLException e) {
			System.out.println("Diretório Incorreto: " + new File("bin"));
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	} 
	
	public ClassLoader createLoader() {
		return new URLClassLoader(new URL[] {binaryDirectory()});
	}
	
	public Class loadPlugin(ClassLoader loader) {
		try {
			clazz = loader.loadClass(plugin);
		} catch (ClassNotFoundException e) {
			System.out.println("Classe não existe: " + "spikes.klaus.classloading.HelloWorld");
			e.printStackTrace();
			System.exit(1);
		}
		return clazz;
	}

	public Class getPlugin() {
		return clazz;
	}
	
}
