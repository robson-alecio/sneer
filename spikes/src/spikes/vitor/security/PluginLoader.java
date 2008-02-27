package spikes.vitor.security;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class PluginLoader {
	
	String _plugin;
	Class<?> clazz;
	
	public PluginLoader(String plugin) {
		super();
		_plugin = plugin;
		
		ClassLoader loader = createLoader();
		clazz = loadPlugin(loader);
	}
	
	public URL binaryDirectory() {
		try {
			return new File("bin").toURI().toURL();
		} catch (MalformedURLException e) {
			System.out.println("Ivalid Directory: " + new File("bin"));
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	} 
	
	public ClassLoader createLoader() {
		return new URLClassLoader(new URL[] {binaryDirectory()});
	}
	
	public Class<?> loadPlugin(ClassLoader loader) {
		try {
			clazz = loader.loadClass(_plugin);
		} catch (ClassNotFoundException e) {
			System.out.println("Class not found: " + _plugin);
			e.printStackTrace();
			System.exit(1);
		}
		return clazz;
	}

	public Class<?> getPlugin() {
		return clazz;
	}
	
}
