package sneer.bricks.hardware.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import sneer.foundation.brickness.Brick;

@Brick
public interface IO {

	Files files();
	Streams streams();
	
	interface Files{
		boolean isEmpty(File file);
		String concat(String basePath, String path);
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
