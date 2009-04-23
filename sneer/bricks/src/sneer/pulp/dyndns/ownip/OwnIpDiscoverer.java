package sneer.pulp.dyndns.ownip;

import sneer.brickness.Brick;
import sneer.pulp.reactive.Signal;

@Brick
public interface OwnIpDiscoverer {
	
	Signal<String> ownIp();

}
