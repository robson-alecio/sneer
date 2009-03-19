package sneer.pulp.dyndns.ownaccount;

import sneer.brickness.Brick;
import sneer.pulp.reactive.Signal;
import wheel.lang.Consumer;

public interface DynDnsAccountKeeper extends Brick {
	
	Signal<DynDnsAccount> ownAccount();

	Consumer<DynDnsAccount> accountSetter();

}
