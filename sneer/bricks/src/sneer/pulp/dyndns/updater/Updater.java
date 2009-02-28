package sneer.pulp.dyndns.updater;

import java.io.IOException;

import sneer.brickness.Brick;

public interface Updater extends Brick {

	void update(String dynDnsHost, String dynDnsUser, String password, String newIp) throws UpdaterException, IOException;

}
