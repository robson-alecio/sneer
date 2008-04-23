package sneer.bricks.deployer;

import java.io.File;
import java.util.List;

public interface BrickBundle {

	void explode(File target) throws Exception;

	List<String> brickNames();

	BrickFile brick(String brickName);
}