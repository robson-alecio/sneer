package sneer.pulp.deployer;

import java.util.List;

import sneer.kernel.container.jar.DeploymentJar;

public interface BrickBundle {

	List<String> brickNames();

	BrickFile brick(String brickName);

	void add(DeploymentJar jar) throws DeployerException;

	void prettyPrint();

	void sort();
}