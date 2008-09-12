package sneer.pulp.deployer.impl;

import java.io.File;
import java.util.List;

interface VirtualDirectoryFactory {

	File rootDirectory();

	List<VirtualDirectory> virtualDirectories();
}
