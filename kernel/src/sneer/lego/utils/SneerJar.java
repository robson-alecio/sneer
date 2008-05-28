package sneer.lego.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import sneer.lego.utils.io.NetworkFriendly;

public interface SneerJar extends Serializable, NetworkFriendly {

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