package sneer.installer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

class Installer {

	private File _sneerHome;
	private File _sneerTmp;

	Installer(File sneerHome) throws IOException {
		_sneerHome = sneerHome;
		_sneerTmp = new File(_sneerHome.getParentFile(), ".sneertmp");
		createDirectory();
		addBinaries();
		renameDirectory();
	}

	private void createDirectory() throws IOException {
		IOUtils.deleteDirectory(_sneerHome);
		IOUtils.deleteDirectory(_sneerTmp);
		_sneerTmp.mkdirs();
	}

	private void renameDirectory() {
		_sneerTmp.renameTo(_sneerHome);
	}
	
	private void addBinaries() throws IOException {
		String jarFileName = this.getClass().getResource("").toString()
									.replace("jar:file:/", "")
									.replace("!/sneer/installer/", "")
									.replace("sneer-bootstrap.jar", "sneer.jar");
		extractFiles(jarFileName);
	}

	private void extractFiles(String jarFileName) throws IOException {
		File src = new File(jarFileName);
		File targetRoot = new File(_sneerTmp, "bin");
		
		IOUtils.write(new  File(_sneerTmp, "log.txt"), "expand files from: " + src.getAbsolutePath());

		if(!(src.exists()))
			throw new IOException("File '" + src.getAbsolutePath() + "' not found!");	
		
		JarInputStream jis = new JarInputStream(new FileInputStream(src));
		JarFile jar = new JarFile(src);
		JarEntry entry = null;
		
        while ((entry = jis.getNextJarEntry()) != null) {
        	File file = new File(targetRoot, entry.getName());

        	if(entry.isDirectory()) {
				file.mkdirs();
				continue;
        	}
        	
        	IOUtils.write(file, IOUtils.readEntryBytes(jar, entry));
        }
	}
}