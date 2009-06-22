package sneer.bricks.software.code.jar;

import java.io.File;
import java.io.IOException;

public interface JarBuilder extends java.io.Closeable{

	void add(String fileName, File classFile) throws IOException;
	
}