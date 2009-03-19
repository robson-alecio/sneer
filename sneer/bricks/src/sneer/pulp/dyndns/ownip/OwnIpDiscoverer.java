package sneer.pulp.dyndns.ownip;

import sneer.brickness.Brick;
import sneer.pulp.reactive.Signal;

public interface OwnIpDiscoverer extends Brick {
	
	Signal<String> ownIp();

}
