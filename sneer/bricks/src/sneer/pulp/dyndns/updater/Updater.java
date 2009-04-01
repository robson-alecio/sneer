package sneer.pulp.dyndns.updater;

import java.io.IOException;

import sneer.brickness.OldBrick;

public interface Updater extends OldBrick {

	void update(String dynDnsHost, String dynDnsUser, String password, String newIp) throws UpdaterException, IOException;

}
