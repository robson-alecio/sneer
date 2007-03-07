package sneer.platform.server;

import java.io.Serializable;


public interface Command extends Serializable {

	void execute();

}
