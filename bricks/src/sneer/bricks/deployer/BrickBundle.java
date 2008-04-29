package sneer.bricks.deployer;

import java.util.List;
import java.util.jar.JarFile;

public interface BrickBundle {

	//void explode(File target) throws Exception;

	List<String> brickNames();

	BrickFile brick(String brickName);

	void add(JarFile jarFile) throws DeployerException;
}