package sneer.bricks.deployer;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import sneer.bricks.dependency.Dependency;
import sneer.bricks.keymanager.PublicKey;
import sneer.lego.jar.SneerJar;
import sneer.lego.utils.InjectedBrick;

public interface BrickFile extends Serializable, Comparable<BrickFile> {

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

	PublicKey origin();
	
	void origin(PublicKey pk);
}
