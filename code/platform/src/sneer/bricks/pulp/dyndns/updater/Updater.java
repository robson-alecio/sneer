package sneer.bricks.pulp.dyndns.updater;

import java.io.IOException;

import sneer.foundation.brickness.Brick;

@Brick
public interface Updater {

	void update(String dynDnsHost, String dynDnsUser, String password, String newIp) throws UpdaterException, IOException;

}
