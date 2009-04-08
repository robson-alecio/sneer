package sneer.pulp.dyndns.ownaccount;

import sneer.brickness.Brick;
import sneer.pulp.reactive.Signal;
import wheel.lang.Consumer;

@Brick
public interface DynDnsAccountKeeper {
	
	Signal<DynDnsAccount> ownAccount();

	Consumer<DynDnsAccount> accountSetter();

}
