package sneerapps.wind;

import sneer.bricks.keymanager.PublicKey;

public interface ProbeFactory {

	Probe produceProbeFor(PublicKey peerPK);

}
