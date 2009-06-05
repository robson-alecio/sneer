package sneer.brickness.testsupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public class IOUtils {
	
	static void write(File file, byte[] bytes) throws IOException {
		file.getParentFile().mkdirs();
		file.createNewFile();
        OutputStream out = new java.io.FileOutputStream(file);
        try {
            out.write(bytes);
        } finally {
            try { out.close(); } catch (Throwable ignore) {}
        }	
	}
	
	static void write(File file, String text) throws IOException {
         write(file, text.getBytes());
	}	
	
	static void deleteDirectory(File directory) throws IOException {
		if (!directory.exists()) return;
		if (!directory.isDirectory()) 
			throw new IllegalArgumentException(directory.getAbsolutePath() + " is not a directory");

		recursiveDelete(directory);

		if (!directory.delete()) 
			throw new IOException("Unable to delete directory: " + directory.getAbsolutePath());
	}

	private static void recursiveDelete(File directory) throws IOException, FileNotFoundException {
		for (File file : directory.listFiles()) {
			if (!file.exists()) 
				throw new FileNotFoundException("File does not exist: " + file.getAbsolutePath());
			
			if (file.isFile() && !file.delete()) 
				throw new IOException(("Unable to delete file: " + file.getAbsolutePath()));
			
			deleteDirectory(file);
		}
	}
}