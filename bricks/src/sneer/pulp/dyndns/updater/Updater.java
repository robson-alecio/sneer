package sneer.pulp.dyndns.updater;

import sneer.kernel.container.Brick;

public interface Updater extends Brick {

	/**
	 * @return true if the record was updated or false if the record was already up to date
	 * @throws UpdaterException
	 */
	boolean update(String dynDnsHost, String dynDnsUser, String password, String newIp) throws UpdaterException;

}
