package sneer.tests.adapters;

import java.io.File;

import sneer.tests.SovereignParty;

public interface SneerParty extends SovereignParty {

	void setSneerPort(int port);
	int sneerPort();

	void configDirectories(File dataFolder, File ownSrcFolder, File Folder, File platformSrcFolder, File platformBinFolder);

	void startSnapps();
	void accelerateHeartbeat();

	void connectTo(SneerParty b);
	
	void crash();

}
