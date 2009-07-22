package sneer.tests.adapters;

import java.io.File;

import sneer.tests.SovereignParty;

public interface SneerParty extends SovereignParty {

	void setSneerPort(int port);
	int sneerPort();

	void configDirectories(File dataDirectory, File ownSrcDirectory, File ownBinDirectory, File platformSrcDirectory, File platformBinDirectory);

	void startSnapps();
	void accelerateHeartbeat();

	void connectTo(SneerParty b);

}
