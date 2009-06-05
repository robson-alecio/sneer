package antlips.antFileGenerator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AntUtils {

	public static byte[] readEntryBytes(JarFile jar, JarEntry entry) throws IOException {
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
	
	public static void write(File file, byte[] bytes) throws IOException {
		file.getParentFile().mkdirs();
		file.createNewFile();
        OutputStream out = new java.io.FileOutputStream(file);
        try {
            out.write(bytes);
        } finally {
            try { out.close(); } catch (Throwable ignore) {}
        }	
	}
	
	public static void write(File file, String text) throws IOException {
         write(file, text.getBytes());
	}	
	
	public static void deleteDirectory(File directory) throws IOException {
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

	public static String readString(InputStream in) throws IOException {
		InputStreamReader input  = new InputStreamReader(in);
		StringWriter output = new StringWriter();
	    char[] buffer = new char[1024 * 4];
		long count1 = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
		    output.write(buffer, 0, n);
		    count1 += n;
		}
		return output.toString();
	}
}