package sneer.pulp.dyndns.ownip;

import sneer.brickness.OldBrick;
import sneer.pulp.reactive.Signal;

public interface OwnIpDiscoverer extends OldBrick {
	
	Signal<String> ownIp();

}
