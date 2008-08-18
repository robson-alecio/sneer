package sneer.pulp.deployer;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import sneer.lego.jar.SneerJar;
import sneer.lego.utils.InjectedBrick;
import sneer.pulp.dependency.Dependency;
import sneer.pulp.keymanager.PublicKey;

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
