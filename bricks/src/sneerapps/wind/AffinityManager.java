package sneerapps.wind;

import sneer.bricks.keymanager.PublicKey;

public interface AffinityManager {

	void setAffinityFor(PublicKey peer, float percentage);

	float affinityFor(PublicKey peer);

}
