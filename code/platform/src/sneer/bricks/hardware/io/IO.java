package sneer.bricks.hardware.io;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;

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
		Collection<File> listFiles(File folder, Filter fileFilter, Filter folderFilter);
	}
	
	interface Filter{
		public boolean accept(File file);
		public boolean accept(File folder, String name);
	}
	
	interface Files{
		boolean isEmpty(File file);
		Collection<File> listFiles(File folder, String[] extensions, boolean recursive);
		
		void copyFolder(File srcFolder, File destFolder) throws IOException;
		void deleteFolder(File folder) throws IOException;
		Iterator<File> iterate(File folder, String[] extensions, boolean recursive);

		void writeString(File file, String data) throws IOException;
		
		byte[] readBytes(File file) throws IOException;
		void readBytes(File file, Consumer<byte[]> content);
		void readBytes(File file, Consumer<byte[]> content, Consumer<IOException> exception);
		
		String readString(File file) throws IOException;
		void readString(File file, Consumer<String> content);
		void readString(File file, Consumer<String> content, Consumer<IOException> exception);
		
		void writeByteArrayToFile(File file, byte[] data) throws IOException;

		boolean contentEquals(File file1, File file2) throws IOException;
	}
	
	interface Streams{
		String toString(InputStream input) throws IOException;
		byte[] toByteArray(InputStream input) throws IOException;
		byte[] readBytesAndClose(InputStream input) throws IOException;
	}
}