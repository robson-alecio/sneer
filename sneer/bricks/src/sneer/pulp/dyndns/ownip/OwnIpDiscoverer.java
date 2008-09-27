package sneer.pulp.dyndns.ownip;

import sneer.kernel.container.Brick;
import wheel.reactive.Signal;

public interface OwnIpDiscoverer extends Brick {
	
	Signal<String> ownIp();

}
