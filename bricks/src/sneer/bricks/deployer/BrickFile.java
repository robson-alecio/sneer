package sneer.bricks.deployer;

import java.io.File;
import java.io.IOException;

import sneer.lego.utils.SneerJar;

public interface BrickFile {

	String name();

	void add(SneerJar jarFile) throws IOException;

	SneerJar api();
	SneerJar apiSrc();

	SneerJar impl();
	SneerJar implSrc();

	void explodeSources(File target) throws IOException;

	BrickFile copyTo(File target) throws IOException;
}
