package sneer.installer;

import java.io.File;

import main.SneerStoragePath;

public class Installer {

	private File _sneerHome;
	private File _sneerTmp;

	void install(SneerStoragePath sneerStoragePath) {
		_sneerHome = new File(sneerStoragePath.get());
		_sneerTmp = new File(_sneerHome.getParentFile(), ".sneertmp");
		createDirectories();
		unzipSneer();
		renameDirectory();
	}

	private void createDirectories() {
		_sneerTmp.delete();
		_sneerTmp.mkdirs();
	}

	private void renameDirectory() {
		_sneerTmp.renameTo(_sneerHome);
	}
	
	private void unzipSneer() {
//		throw new sneer.commons.lang.exceptions.NotImplementedYet(); // Implement
	}
}
