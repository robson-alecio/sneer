package sneer.bricks.pulp.dyndns.ownip;

import sneer.bricks.pulp.reactive.Signal;
import sneer.foundation.brickness.Brick;

@Brick
public interface OwnIpDiscoverer {
	
	Signal<String> ownIp();

}
