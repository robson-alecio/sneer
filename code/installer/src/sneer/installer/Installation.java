package sneer.installer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

import sneer.main.SneerDirectories;

import static sneer.main.SneerDirectories.SNEER_HOME;
import static sneer.main.SneerDirectories.PLATFORM_CODE;

class Installation {

	private final URL jarFileName = this.getClass().getResource("/sneer.jar");
	
	Installation() throws IOException {
		cleanup();
		createDirectories();
		addBinaries();
	}

	private void cleanup() throws IOException {
		PLATFORM_CODE.delete();
	}
	
	private void createDirectories() throws IOException {
		if(!SNEER_HOME.exists())
			SNEER_HOME.mkdirs();
		PLATFORM_CODE.mkdirs();
	}

	private void addBinaries() throws IOException {
		IOUtils.write(SneerDirectories.LOG_FILE, "jar file url: " + jarFileName.toString());
		File file = extractJar(jarFileName);
		extractFiles(file);
	}

	private File extractJar(URL url) throws IOException {
		File file =  File.createTempFile("sneer", ".jar");
		file.deleteOnExit();

		InputStream input = url.openStream();
		IOUtils.copyToFile(input, file);
		input.close();
		return file;
	}
	
	private void extractFiles(File src) throws IOException {
		IOUtils.write(SneerDirectories.LOG_FILE, "expand files from: " + src.getAbsolutePath());
		if(!(src.exists()))
			throw new IOException("File '" + src.getAbsolutePath() + "' not found!");	

		FileInputStream inputStream = new FileInputStream(src);
		extractFiles(src, inputStream);
		inputStream.close();
	}

	private void extractFiles(File src, FileInputStream inputStream) throws IOException {
		JarInputStream jis = new JarInputStream(inputStream);
		JarFile jar = new JarFile(src);
		JarEntry entry = null;
		
        while ((entry = jis.getNextJarEntry()) != null) {
        	File file = new File(PLATFORM_CODE, entry.getName());

        	if(entry.isDirectory()) {
        		file.mkdirs();
				continue;
        	}
        	IOUtils.writeEntry(jar, entry, file);
        }
	}
}