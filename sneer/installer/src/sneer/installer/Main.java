package sneer.installer;

import java.io.File;

import main.Sneer;
import main.SneerStoragePath;

public class Main {
	
	public static void main(String[] args) {
		SneerStoragePath sneerStoragePath = new SneerStoragePath();
		File sneerHome = new File(sneerStoragePath.get());
		
		if(sneerHome.exists() && sneerHome.isDirectory()){
			try {
				Sneer.main(args);
			} catch (Exception e) {
				throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
			}
			return;
		}
		
		new Wizard(sneerHome);
	}
}
