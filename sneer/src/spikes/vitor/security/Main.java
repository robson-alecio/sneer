package spikes.vitor.security;


public class Main {

	public static void main (String[] args) {
		ThreadGroup plugins = new ThreadGroup("Threads of Plugins");
		
		System.setSecurityManager(new SneerSecurityManager(plugins));
		
		PluginLoader loader = new PluginLoader("spikes.klaus.classloading.HelloWorld");
		PluginRunner runner = new PluginRunner(plugins, loader.getPlugin());

		runner.start();
		
		loader = new PluginLoader("spikes.vitor.security.BadFilePlugin");
		runner = new PluginRunner(plugins, loader.getPlugin());		
		
		runner.start();
		
		loader = new PluginLoader("spikes.vitor.security.BadImportPlugin");
		runner = new PluginRunner(plugins, loader.getPlugin());		
		
		runner.start();	
	}
	
}
