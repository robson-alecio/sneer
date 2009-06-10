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

	private final String _jarToDownload = "http://sovereigncomputing.net/hudson/job/Sneer/lastBuild/artifact/code/build/dist/sneer.jar";

	private File _sneerHome;
	private File _sneerTmp;
	private File _sneerTmpBin;

	Installation(File sneerHome) throws IOException {
		_sneerHome = sneerHome;
		_sneerTmp = new File(_sneerHome.getParentFile(), ".sneertmp");
		_sneerTmpBin = new File(_sneerTmp, "bin");
		
		createDirectory();
		addBinaries();
		renameDirectory();
	}

	private void createDirectory() throws IOException {
		IOUtils.deleteDirectory(_sneerHome);
		IOUtils.deleteDirectory(_sneerTmp);
		_sneerTmp.mkdirs();
	}

	private void renameDirectory() throws IOException {
		if(!_sneerTmp.renameTo(_sneerHome))
			throw new IOException(_sneerTmp.getAbsolutePath() + " can't renamed to " + _sneerHome.getAbsolutePath());	
	}
	
	private void addBinaries() throws IOException {
		File file = download(new URL(_jarToDownload));
		extractFiles(file);
	}

	private File download(URL url) throws IOException {
		File file =  File.createTempFile("sneer", ".jar");
		file.deleteOnExit();

		InputStream input = url.openStream();
		IOUtils.copyToFile(input, file);
		input.close();
		return file;
	}
	
	private void extractFiles(File src) throws IOException {
		IOUtils.write(new  File(_sneerTmp, "log.txt"), "expand files from: " + src.getAbsolutePath());
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
        	File file = new File(_sneerTmpBin, entry.getName());

        	if(entry.isDirectory()) {
				file.mkdirs();
				continue;
        	}
        	
        	IOUtils.write(file, IOUtils.readEntryBytes(jar, entry));
        }
	}
}