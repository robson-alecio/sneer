package sneer.bricks.deployer;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

public interface BrickFile {

	String name();

	void add(JarFile jarFile) throws IOException;

	JarFile api();

	JarFile impl();

	void explode(File target) throws IOException;

	void copyTo(File target) throws IOException;
}
