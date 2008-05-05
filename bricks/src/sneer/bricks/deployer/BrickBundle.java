package sneer.bricks.deployer;

import java.util.List;

import sneer.lego.utils.SneerJar;

public interface BrickBundle {

	List<String> brickNames();

	BrickFile brick(String brickName);

	void add(SneerJar jar) throws DeployerException;
}