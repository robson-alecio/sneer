package wheel.io.files.impl;

import java.io.IOException;

public interface CloseableWithListener {
	
	void close() throws IOException;

	void notifyOnClose(Listener listener);
	public interface Listener {
		void streamClosed(CloseableWithListener stream);
	}
	
}
