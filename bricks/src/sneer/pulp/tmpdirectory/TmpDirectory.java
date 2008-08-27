package sneer.pulp.tmpdirectory;

import java.io.File;
import java.io.IOException;

public interface TmpDirectory {

	File createTempFile(String suffix) throws IOException;

	File createTempDirectory(String dirName) throws IOException;

}
