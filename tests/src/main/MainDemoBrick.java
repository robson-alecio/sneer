package main;

import sneer.pulp.dyndns.ownaccount.DynDnsAccount;

public interface MainDemoBrick {

	void start(String ownName, int port, DynDnsAccount dynDnsAccount);

}
