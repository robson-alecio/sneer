package sneer.pulp.deployer;

import java.util.List;

import sneer.kernel.container.jar.SneerJar;

public interface BrickBundle {

	List<String> brickNames();

	BrickFile brick(String brickName);

	void add(SneerJar jar) throws DeployerException;

	void prettyPrint();

	void sort();
}