package snapps.wind;

import sneer.brickness.OldBrick;
import sneer.pulp.reactive.collections.ListSignal;
import wheel.lang.Consumer;

public interface Wind extends OldBrick {

	ListSignal<Shout> shoutsHeard();

	Consumer<String> megaphone();

}
