package sneer.bricks.hardware.io;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import sneer.foundation.brickness.Brick;

@Brick
public interface IO {

	void crash(Closeable closeable);
	
	Files files();
	FileFilters fileFilters();
	Streams streams();
	
	interface FileFilters{
		Filter not(Filter filter);
		Filter suffix(String sulfix);
		Filter name(String name);
		Collection<File> listFiles(File directory, Filter file, Filter dir);
	}
	
	interface Filter{
		public boolean accept(File file);
		public boolean accept(File dir, String name);
	}
	
	interface Files{
		boolean isEmpty(File file);
		String concat(String basePath, String path);
		void copyDirectory(File srcDir, File destDir) throws IOException;
		Collection<File> listFiles(File directory, String[] extensions, boolean recursive);
		void writeStringToFile(File file, String data) throws IOException;
		void deleteDirectory(File directory) throws IOException;
		Iterator<File> iterate(File directory, String[] extensions, boolean recursive);
		byte[] readFileToByteArray(File file) throws IOException;
	}
	
	interface Streams{
		String toString(InputStream input) throws IOException;
		byte[] toByteArray(InputStream input) throws IOException;
		byte[] readBytesAndClose(InputStream input) throws IOException;
	}
}