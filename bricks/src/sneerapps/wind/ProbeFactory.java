package sneerapps.wind;

import sneer.bricks.keymanager.PublicKey;
import sneerapps.wind.ConnectionSide;

public interface ProbeFactory {

	Probe createProbeFor(PublicKey peerPK, ConnectionSide mySide);

}
