package snapps.wind;

import snapps.wind.ConnectionSide;
import sneer.pulp.keymanager.PublicKey;

public interface ProbeFactory {

	Probe createProbeFor(PublicKey peerPK, ConnectionSide mySide);

}
