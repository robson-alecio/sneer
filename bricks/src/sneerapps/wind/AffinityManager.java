package sneerapps.wind;

import sneer.pulp.keymanager.PublicKey;

public interface AffinityManager {

	void setAffinityFor(PublicKey peer, float percentage);

	float affinityFor(PublicKey peer);

}
