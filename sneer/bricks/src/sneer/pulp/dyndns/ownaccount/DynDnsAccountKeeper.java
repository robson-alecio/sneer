package sneer.pulp.dyndns.ownaccount;

import sneer.brickness.Brick;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.Signal;

@Brick
public interface DynDnsAccountKeeper {
	
	Signal<DynDnsAccount> ownAccount();

	Consumer<DynDnsAccount> accountSetter();

}
