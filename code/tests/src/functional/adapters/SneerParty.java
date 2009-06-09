package functional.adapters;

import sneer.brickness.Brick;
import sneer.brickness.PublicKey;
import functional.SovereignParty;

@Brick
public interface SneerParty extends SovereignParty {

	PublicKey publicKey();

	void setSneerPort(int port);
	int sneerPort();

}
