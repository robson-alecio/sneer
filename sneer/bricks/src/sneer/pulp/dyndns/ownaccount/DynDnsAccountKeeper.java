package sneer.pulp.dyndns.ownaccount;

import sneer.brickness.Brick;
import wheel.lang.Consumer;
import wheel.reactive.Signal;

public interface DynDnsAccountKeeper extends Brick {
	
	Signal<DynDnsAccount> ownAccount();

	Consumer<DynDnsAccount> accountSetter();

}
