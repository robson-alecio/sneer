package sneer.installer;

import java.io.File;

import main.SneerStoragePath;

public class Main {
	
	public static void main(String[] args) {
		try {
			SneerStoragePath sneerStoragePath = new SneerStoragePath();
			File sneerHome = new File(sneerStoragePath.get());
			
			if(sneerHome.exists() && sneerHome.isDirectory()){
				new SneerJockey(sneerStoragePath);
				return;
			}
			
			new Wizard(sneerStoragePath);
		} catch (Throwable throwable) {
			Dialogs.show("Sneer Error:", throwable.getMessage(),	"Exit");
			System.exit(1);
		}
	}
}