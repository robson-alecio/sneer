package sneer.pulp.dependency;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface DependencyManager {

	List<FileWithHash> dependenciesFor(String brickName);

	FileWithHash add(String brickName, FileWithHash dependency) throws IOException;

	FileWithHash newDependency(File file);
}
