package sneerapps.wind;

import sneer.bricks.keymanager.PublicKey;

public interface AffinityManager {

	void setAffinityFor(PublicKey peer, float percentage);

	Float affinityFor(PublicKey peer);

}
