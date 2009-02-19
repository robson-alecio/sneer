package sneer.pulp.deployer;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import sneer.kernel.container.PublicKey;
import sneer.kernel.container.jar.DeploymentJar;
import sneer.pulp.dependency.FileWithHash;

public interface BrickFile extends Serializable, Comparable<BrickFile> {

	String name();

	void add(DeploymentJar jarFile) throws IOException;

	DeploymentJar api();
	DeploymentJar apiSrc();

	DeploymentJar impl();
	DeploymentJar implSrc();

	void explodeSources(File target) throws IOException;

	BrickFile copyTo(File target) throws IOException;
	
	List<FileWithHash> fileDependencies();

	Iterable<String> brickDependencies() throws IOException;

	void resolved(boolean resolved);

	boolean resolved();

	PublicKey origin();
	
	void origin(PublicKey pk);
}
