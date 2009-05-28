package sneer.installer;

import java.io.File;

import main.SneerStoragePath;

public class Main {

	public static void main(String[] args) throws Exception {
		SneerStoragePath sneerStoragePath = new SneerStoragePath();
		File sneerHome = new File(sneerStoragePath.get());

		if (sneerHome.exists() && sneerHome.isDirectory()) {
			new SneerJockey(sneerStoragePath);
			return;
		}

		new Wizard(sneerStoragePath);
	}
}