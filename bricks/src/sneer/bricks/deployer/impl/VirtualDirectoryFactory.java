package sneer.bricks.deployer.impl;

import java.io.File;
import java.util.List;

public interface VirtualDirectoryFactory {

	File rootDirectory();

	List<VirtualDirectory> brickDirectories();
}
