package sneer.pulp.deployer.impl;

import java.io.File;
import java.util.List;

public interface VirtualDirectoryFactory {

	File rootDirectory();

	List<VirtualDirectory> virtualDirectories();
}
