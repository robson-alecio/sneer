package sneerapps.wind;

import sneer.pulp.keymanager.PublicKey;
import sneerapps.wind.ConnectionSide;

public interface ProbeFactory {

	Probe createProbeFor(PublicKey peerPK, ConnectionSide mySide);

}
