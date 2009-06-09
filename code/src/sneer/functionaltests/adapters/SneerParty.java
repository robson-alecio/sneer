package sneer.functionaltests.adapters;

import sneer.foundation.brickness.Brick;
import sneer.foundation.brickness.PublicKey;
import sneer.functionaltests.SovereignParty;

@Brick
public interface SneerParty extends SovereignParty {

	PublicKey publicKey();

	void setSneerPort(int port);
	int sneerPort();

}
