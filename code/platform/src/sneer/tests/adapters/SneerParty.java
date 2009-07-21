package sneer.tests.adapters;

import java.io.File;

import sneer.foundation.brickness.Brick;
import sneer.tests.SovereignParty;

@Brick
public interface SneerParty extends SovereignParty {

	void setSneerPort(int port);
	int sneerPort();

	void setBinDirectories(File ownBinDirectory, File platformBinDirectory);
	void setDataDirectory(File dataDirectory);

	void startSnapps();
	void accelerateHeartbeat();

	void connectTo(SneerParty b);

}
