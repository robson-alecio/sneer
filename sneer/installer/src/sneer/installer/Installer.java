package sneer.installer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

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
		FileUtils.deleteDirectory(_sneerHome);
		FileUtils.deleteDirectory(_sneerTmp);
		_sneerTmp.mkdirs();
	}

	private void renameDirectory() {
		_sneerTmp.renameTo(_sneerHome);
	}
	
	private void addBinaries() throws IOException {
		String jarFileName = this.getClass().getResource("").toString()
									.replace("jar:file:/", "")
									.replace("!/sneer/installer/", "");
			
		extractFiles(jarFileName);
	}

	private void extractFiles(String jarFileName) throws IOException {
		File src = new File(jarFileName);
		File targetRoot = new File(_sneerTmp, "bin");
		
		FileUtils.writeStringToFile(new  File(_sneerTmp, "log.txt"), "expand files from: " + src.getAbsolutePath());

		if(!(src.exists() && src.isFile()))
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
        	
			FileUtils.writeByteArrayToFile(file, readEntryBytes(jar, entry));
        }
	}
	
	private byte[] readEntryBytes(JarFile jar, JarEntry entry) throws IOException{
		final InputStream is = jar.getInputStream(entry);
		try {
			return IOUtils.toByteArray(is);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}
	

}