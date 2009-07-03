package sneer.installer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

class Installation {

	private final URL jarFileName = this.getClass().getResource("/sneer.jar");
	private final URL ownFileName = this.getClass().getResource("/own.jar");
	
	Installation() throws IOException {
		cleanup();
		createDirectories();
		addBinaries();
		createOwnProjectIfNecessary();
	}

	private void createOwnProjectIfNecessary() throws IOException {
		if(ownCode().exists()) return;
		
		IOUtils.write(logFile(), "jar file url: " + ownFileName.toString());
		File file = extractJar(ownFileName, "own", "jar");
		extractFiles(file, ownCode().getParentFile());		
	}

	private void cleanup() throws IOException {
		platformCode().delete();
	}

	private void createDirectories() throws IOException {
		if(!sneerHome().exists())
			sneerHome().mkdirs();
		
		if(platformCode().exists())
			platformCode().delete();
		
		platformCode().mkdirs();
	}

	private void addBinaries() throws IOException {
		IOUtils.write(logFile(), "jar file url: " + jarFileName.toString());
		File file = extractJar(jarFileName, "sneer", "jar");
		extractFiles(file, platformCode());
	}

	private File extractJar(URL url, String prefix, String suffix) throws IOException {
		File file =  File.createTempFile(prefix, suffix);
		file.deleteOnExit();

		InputStream input = url.openStream();
		IOUtils.copyToFile(input, file);
		input.close();
		return file;
	}
	
	private void extractFiles(File src, File toDir) throws IOException {
		IOUtils.write(logFile(), "expand files from: " + src.getAbsolutePath());
		if(!(src.exists()))
			throw new IOException("File '" + src.getAbsolutePath() + "' not found!");	

		FileInputStream inputStream = new FileInputStream(src);
		extractFiles(src, toDir, inputStream);
		inputStream.close();
	}

	private void extractFiles(File src, File toDir, FileInputStream inputStream) throws IOException {
		JarInputStream jis = new JarInputStream(inputStream);
		JarFile jar = new JarFile(src);
		JarEntry entry = null;
		
        while ((entry = jis.getNextJarEntry()) != null) {
        	File file = new File(toDir, entry.getName());

        	if(entry.isDirectory()) {
        		file.mkdirs();
				continue;
        	}
        	IOUtils.writeEntry(jar, entry, file);
        }
	}

	private File ownCode() { return Directories.OWN_CODE(); }
	private File platformCode() { return Directories.PLATFORM_CODE(); }
	private File sneerHome() { return Directories.SNEER_HOME(); }
	private File logFile() { return Directories.LOG_FILE(); }
}