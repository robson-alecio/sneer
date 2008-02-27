package wheel.io.serialization;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;

public class OptimizedSerializer {

	private final ByteArrayOutputStream _outputBytes = new ByteArrayOutputStream();
	private final ObjectOutputStream _outputStream;
	private final int _resetBatchSize;
	private int _resetBatchIndex = 0;

	public OptimizedSerializer(int resetBatchSize) {
		_resetBatchSize = resetBatchSize;
		try {
			_outputStream = new ObjectOutputStream(_outputBytes);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public byte[] serialize(Object object) throws NotSerializableException {
		try {
			_outputStream.writeObject(object);
			resetIfNecessary();
			_outputStream.flush();
			
			return _outputBytes.toByteArray();
			
		} catch (NotSerializableException nse) {
			throw nse;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		} finally {
			_outputBytes.reset();
		}
	}

	private void resetIfNecessary() throws IOException {
		if (++_resetBatchIndex < _resetBatchSize) return;

		_resetBatchIndex = 0;
		_outputStream.reset();
	}

}
