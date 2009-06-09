package functionaltests.adapters;

import sneer.brickness.Brick;
import sneer.brickness.PublicKey;
import functionaltests.SovereignParty;

@Brick
public interface SneerParty extends SovereignParty {

	PublicKey publicKey();

	void setSneerPort(int port);
	int sneerPort();

}
