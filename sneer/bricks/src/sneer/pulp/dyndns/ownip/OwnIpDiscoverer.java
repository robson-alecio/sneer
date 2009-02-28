package sneer.pulp.dyndns.ownip;

import sneer.brickness.Brick;
import wheel.reactive.Signal;

public interface OwnIpDiscoverer extends Brick {
	
	Signal<String> ownIp();

}
