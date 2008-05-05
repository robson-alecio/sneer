package sneer.lego.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface SneerJar {

	File file();

	String brickName();

	String role();

	byte[] sneer1024();
	
	void add(String entryName, File file) throws IOException;

	void add(String entryName, String contents) throws IOException;

	void close() throws IOException;

	InputStream getInputStream(String entryName) throws IOException;
	
	void explode(File target) throws IOException;

}