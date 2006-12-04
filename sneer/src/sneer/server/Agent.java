package sneer.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface Agent {

	void helpYourself(ObjectInputStream objectIn, ObjectOutputStream objectOut) throws Exception;

}
