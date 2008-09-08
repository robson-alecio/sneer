package sneer.pulp.dyndns.ownaccount;

import sneer.kernel.container.Brick;
import wheel.reactive.Signal;

public interface OwnAccountKeeper extends Brick {
	
	Signal<Account> ownAccount();

}
