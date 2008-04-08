package sneer.bricks.deployer.impl;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface SourceMeta {

	File rootDirectory();

	List<File> interfaces();

	Map<File,List<File>> interfacesByBrick();
	
	Map<File,List<File>> implByBrick();

}
