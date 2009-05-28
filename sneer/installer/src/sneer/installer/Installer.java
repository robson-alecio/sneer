package sneer.installer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import main.SneerStoragePath;
import java.util.jar.*;

class Installer {

	private File _sneerHome;
	private File _sneerTmp;

	Installer(SneerStoragePath sneerStoragePath) throws IOException {
		_sneerHome = new File(sneerStoragePath.get());
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
		try {
			copyFlies(this.getClass().getResource ("../../").getFile());
			
		} catch (NullPointerException e) {
			String jarFileName = this.getClass().getResource("").toString()
										.replace("jar:file:/", "")
										.replace("!/sneer/installer/", "");
			
			extractFiles(jarFileName);
		}
	}

	private void copyFlies(String srcBin) throws IOException {
		File srcBinFile = new File(srcBin);
		if(!(srcBinFile.exists() && srcBinFile.isDirectory()))
			throw new IOException("Directory '" + srcBinFile.getAbsolutePath() + "' not found!");

		FileUtils.writeStringToFile(new  File(_sneerTmp, "log.txt"), "copy files from: " + srcBinFile.getAbsolutePath());
		FileUtils.copyDirectory(srcBinFile, new File(_sneerTmp, "bin"));
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
	
	public static void main(String[] args) throws IOException {
		new Installer(new SneerStoragePath());
	}
}