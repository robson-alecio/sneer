package sneer.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public interface Agent extends Serializable {

	void helpYourself(ObjectInputStream objectIn, ObjectOutputStream objectOut) throws Exception;

}
