package functional.adapters;

import sneer.brickness.PublicKey;
import sneer.container.NewBrick;
import functional.SovereignParty;

@NewBrick
public interface SneerParty extends SovereignParty {

	PublicKey publicKey();

	void setSneerPort(int port);
	int sneerPort();

}
