package sneer.pulp.dyndns.updater;

import sneer.kernel.container.Brick;
import sneer.pulp.dyndns.ownaccount.Account;

public interface Updater extends Brick {

	/**
	 * 
	 * @param account account information
	 * @param ip the new ip address
	 * @return true if the record was updated or false if the record was already up to date
	 * @throws UpdaterException
	 */
	boolean update(Account account, String ip) throws UpdaterException;

}
