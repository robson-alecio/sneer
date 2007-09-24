package wheel.io.serialization;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;

public class DefaultSerializer {

	public DefaultSerializer() {

	}
	
	public byte[] serialize(Object object) throws NotSerializableException {
		ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(outputBytes);
			outputStream.writeObject(object);
			outputStream.close();
			
			return outputBytes.toByteArray();
			
		} catch (NotSerializableException nse) {
			throw nse;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}


}
