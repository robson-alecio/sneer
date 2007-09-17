package wheel.io.files;

import java.io.IOException;
import java.io.OutputStream;

public interface File {

	public void seek(long index) throws IOException;
	
	public OutputStream outputstream();
	
	long modificationTime() throws IOException;

}
