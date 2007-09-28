package sneer.apps.asker.packet;

import java.io.Serializable;

public abstract class AskerRequestPayload implements Serializable{
	
	public abstract String prompt();

}
