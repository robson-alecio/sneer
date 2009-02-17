package sneer.kernel.container.jar;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public interface DeploymentJar extends Serializable, Closeable {

	File file();

	String brickName();

	List<String> injectedBricks() throws IOException;

	String role();

	byte[] sneer1024();
	
	void explode(File target) throws IOException;
}