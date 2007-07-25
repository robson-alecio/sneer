package wheel.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Streams {

	public static void copy(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[4096];
		
		int read;
		while ((read = input.read(buffer)) != -1)
			output.write(buffer, 0, read);
		
		output.flush();
	}

	public static void crash(Closeable closeable) {
		try {
			closeable.close();
		} catch (IOException e) {
			Log.log(e);
		}
		
	}

}