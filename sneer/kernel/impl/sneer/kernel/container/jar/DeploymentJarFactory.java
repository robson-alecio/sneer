package sneer.kernel.container.jar;

import java.io.File;


public interface DeploymentJarFactory {

	DeploymentJar create(File jarFile);

}
