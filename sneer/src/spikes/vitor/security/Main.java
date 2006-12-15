package spikes.vitor.security;

import javax.swing.JFrame;

import spikes.vitor.security.plugins.Plugin;


public class Main {

	static JFrame mainFrame;
	
	public static void main (String[] args) {
		ThreadGroup plugins = new ThreadGroup("Threads of Plugins");
		
		mainFrame = new JFrame("Sneer");
		mainFrame.setBounds(100, 100, 400, 400);
		mainFrame.setVisible(true);
				
		System.setSecurityManager(new SneerSecurityManager(System.getSecurityManager(),plugins));
		
		//PluginLoader loader = new PluginLoader("spikes.klaus.classloading.HelloWorld");
		//PluginRunner runner = new PluginRunner(plugins, loader.getPlugin());

		//runner.start();
		
		//loader = new PluginLoader("spikes.vitor.security.BadFilePlugin");
		//runner = new PluginRunner(plugins, loader.getPlugin());		
		
		//runner.start();
		
		//PluginLoader loader = new PluginLoader("spikes.vitor.security.BadImportPlugin");
		//PluginRunner runner = new PluginRunner(plugins, loader.getPlugin());		
		
		//runner.start();
		
		Thread runner = new Thread(plugins, "Swing plugin") {
			public void run() {
				Plugin plugin = new Plugin(mainFrame);
			}
		};
		runner.start();
		
		
	}
	
	
}
