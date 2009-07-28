package sneer.installer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class IOUtils {

	static byte[] readEntryBytes(JarFile jar, JarEntry entry) throws IOException {
		final InputStream is = jar.getInputStream(entry);
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			
			int n = 0;
			while (-1 != (n = is.read(buffer)))
				output.write(buffer, 0, n);

			return output.toByteArray();
		} finally {
			try { is.close(); } catch (Throwable ignore) { }
		}
	}	
	
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
	
	static void deleteFolder(File folder) throws IOException {
		if (!folder.exists()) return;
		if (!folder.isDirectory()) 
			throw new IllegalArgumentException(folder.getAbsolutePath() + " is not a folder");

		deleteContents(folder);

		if (!folder.delete()) 
			throw new IOException("Unable to delete folder: " + folder.getAbsolutePath());
	}

	private static void deleteContents(File folder) throws IOException, FileNotFoundException {
		File[] files = folder.listFiles();
		if (files == null) return;
		
		for (File file : files) deleteFile(file);
	}

	private static void deleteFile(File file) throws FileNotFoundException, IOException {
		if (!file.exists()) 
			throw new FileNotFoundException("File does not exist: " + file.getAbsolutePath());
		
		if (file.isFile() && !file.delete()) 
			throw new IOException(("Unable to delete file: " + file.getAbsolutePath()));
		
		deleteFolder(file);
	}

	static void copyToFile(InputStream input, File file) throws IOException {
		file.getParentFile().mkdirs();
		file.createNewFile();
		
		OutputStream output = new java.io.FileOutputStream(file);
		try {
		    byte[] buffer = new byte[1024 * 4];
			long count1 = 0;
			int n = 0;
			while (-1 != (n = input.read(buffer))) {
			    output.write(buffer, 0, n);
			    count1 += n;
			}
        } finally {
            try { output.close(); } catch (Throwable ignore) {}
        }	
	}

	static void writeEntry(JarFile jar, JarEntry entry, File file) throws IOException {
		final InputStream is = jar.getInputStream(entry);
		FileOutputStream output = new FileOutputStream(file);
		try {
			byte[] buffer = new byte[1024];
			
			int n = 0;
			while (-1 != (n = is.read(buffer)))
				output.write(buffer, 0, n);

		} finally {
			try { is.close(); } catch (Throwable ignore) { }
			try { output.close(); } catch (Throwable ignore) { }
		}		
	}
}