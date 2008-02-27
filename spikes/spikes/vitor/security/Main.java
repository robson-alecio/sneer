package spikes.vitor.security;

import java.security.Policy;

import javax.swing.JFrame;

import spikes.vitor.security.plugins.Plugin;


public class Main {

	static JFrame mainFrame;
	
	public static void main (String[] args) {
		Policy.setPolicy(new PolicySpike());
		
		
		ThreadGroup plugins = new ThreadGroup("Threads of Plugins");
		
		mainFrame = new JFrame("Sneer");
		mainFrame.setBounds(100, 100, 400, 400);
		mainFrame.setVisible(true);
				
//		System.setSecurityManager(new SneerSecurityManager(System.getSecurityManager(),plugins));
		System.setSecurityManager(new SecurityManager());
		
		
		Thread runner = new Thread(plugins, "Swing plugin") {
			@Override
			public void run() {
				//new URLClassLoader();
				new Plugin(mainFrame);
			}
		};
		runner.start();
		
		
	}
	
	
}
