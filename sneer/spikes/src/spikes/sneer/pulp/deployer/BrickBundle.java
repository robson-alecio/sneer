package spikes.sneer.pulp.deployer;

import java.util.List;

import spikes.sneer.kernel.container.jar.DeploymentJar;

public interface BrickBundle {

	List<String> brickNames();

	BrickFile brick(String brickName);

	void add(DeploymentJar jar) throws DeployerException;

	void sort();
}