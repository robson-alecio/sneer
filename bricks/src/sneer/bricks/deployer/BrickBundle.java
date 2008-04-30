package sneer.bricks.deployer;

import java.util.List;
import java.util.jar.JarFile;

public interface BrickBundle {

	List<String> brickNames();

	BrickFile brick(String brickName);

	void add(JarFile jarFile) throws DeployerException;
}