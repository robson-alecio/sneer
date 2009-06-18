package sneer.bricks.pulp.dyndns.ownaccount;

import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Consumer;

@Brick
public interface DynDnsAccountKeeper {
	
	Signal<DynDnsAccount> ownAccount();

	Consumer<DynDnsAccount> accountSetter();

}
