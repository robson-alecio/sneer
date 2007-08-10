package wheel.io.files;

import java.io.IOException;

public interface File {

	long modificationTime() throws IOException;

}
