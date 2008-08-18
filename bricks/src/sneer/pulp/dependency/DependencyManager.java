package sneer.pulp.dependency;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface DependencyManager {

	List<Dependency> dependenciesFor(String brickName);

	Dependency add(String brickName, Dependency dependency) throws IOException;

	Dependency newDependency(File file);
}
