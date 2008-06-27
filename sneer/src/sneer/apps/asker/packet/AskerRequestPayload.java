package sneer.apps.asker.packet;

import java.io.Serializable;

public abstract class AskerRequestPayload implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public abstract String prompt();

}
