package sneer.pulp.dyndns.ownaccount;

import sneer.brickness.OldBrick;
import sneer.pulp.reactive.Signal;
import wheel.lang.Consumer;

public interface DynDnsAccountKeeper extends OldBrick {
	
	Signal<DynDnsAccount> ownAccount();

	Consumer<DynDnsAccount> accountSetter();

}
