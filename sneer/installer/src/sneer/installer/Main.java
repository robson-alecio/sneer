package sneer.installer;

import java.io.File;

import main.SneerStoragePath;
import sneer.brickness.Brickness;
import sneer.brickness.BricknessFactory;
import sneer.pulp.threads.Threads;

public class Main {
	
	public static void main(String[] args) {
		SneerStoragePath sneerStoragePath = new SneerStoragePath();
		File sneerHome = new File(sneerStoragePath.get());
		
		if(sneerHome.exists() && sneerHome.isDirectory()){
			Brickness container = BricknessFactory.newBrickContainer(sneerStoragePath);
			File classRootDirectory = new File(sneerHome, "bin");
			container.placeBrick(classRootDirectory, Threads.class.getName());
			return;
		}
		
		new Wizard(sneerHome);
	}
}
