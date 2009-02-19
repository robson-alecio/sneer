package sneer.pulp.deployer.impl;

import java.io.File;
import java.util.List;

interface BrickImplDirectoryFactory {

	File rootDirectory();

	List<BrickImplDirectory> implDirectories();
}
