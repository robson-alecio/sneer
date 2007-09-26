package sneer.apps.asker.packet;

import java.io.Serializable;

public interface AskerRequestPayload extends Serializable{
	
	public String prompt();

}
