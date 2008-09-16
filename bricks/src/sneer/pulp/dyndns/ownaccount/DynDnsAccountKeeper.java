package sneer.pulp.dyndns.ownaccount;

import sneer.kernel.container.Brick;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;

public interface DynDnsAccountKeeper extends Brick {
	
	Signal<DynDnsAccount> ownAccount();

	Omnivore<DynDnsAccount> accountSetter();

}
