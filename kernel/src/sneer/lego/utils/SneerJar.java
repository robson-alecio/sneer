package sneer.lego.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface SneerJar {

	File file();

	String brickName();

	List<InjectedBrick> injectedBricks() throws IOException;

	String role();

	byte[] sneer1024();
	
	void add(String entryName, File file) throws IOException;

	void add(String entryName, String contents) throws IOException;

	void close() throws IOException;

	InputStream getInputStream(String entryName) throws IOException;
	
	void explode(File target) throws IOException;


}