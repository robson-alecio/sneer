package sneer.bricks.deployer;

import java.io.File;
import java.io.IOException;
import java.util.List;

import sneer.bricks.dependency.Dependency;
import sneer.lego.utils.InjectedBrick;
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
	
	List<Dependency> dependencies();

	List<InjectedBrick> injectedBricks() throws IOException;

	void resolved(boolean resolved);

	boolean resolved();
}
