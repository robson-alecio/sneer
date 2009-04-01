package functional.adapters;

import sneer.brickness.PublicKey;
import sneer.container.Brick;
import functional.SovereignParty;

@Brick
public interface SneerParty extends SovereignParty {

	PublicKey publicKey();

	void setSneerPort(int port);
	int sneerPort();

}
