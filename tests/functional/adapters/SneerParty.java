package functional.adapters;

import sneer.pulp.keymanager.PublicKey;
import functional.SovereignParty;

public interface SneerParty extends SovereignParty {

	PublicKey publicKey();

	void setSneerPort(int port);
	int sneerPort();

}
