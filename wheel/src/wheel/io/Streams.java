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
			if(closeable!=null) closeable.close();
		} catch (IOException ignored) {}
	}

	public static void crash(Object closeable){
		if(closeable==null)	return;
		Class<?> clazz = closeable.getClass();
		try {
			clazz.getMethod("close", new Class[0]).invoke(closeable, new Object[0]);
		} catch (Exception e) {
			Throwable cause = e.getCause();
			if(cause==null)	Log.log(e);
			else Log.log(cause);
		}
	}

}