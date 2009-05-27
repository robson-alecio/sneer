package sneer.installer;

import java.io.File;

import main.SneerStoragePath;

public class Main {
	
	public static void main(String[] args) {
		SneerStoragePath sneerStoragePath = new SneerStoragePath();
		File sneerHome = new File(sneerStoragePath.get());
		
		if(sneerHome.exists() && sneerHome.isDirectory()){
			new Runner().start(sneerStoragePath);
			return;
		}
		
		new Wizard(sneerStoragePath);
	}
}