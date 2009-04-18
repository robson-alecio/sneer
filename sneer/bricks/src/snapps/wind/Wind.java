package snapps.wind;

import sneer.brickness.OldBrick;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.collections.ListSignal;

public interface Wind extends OldBrick {

	ListSignal<Shout> shoutsHeard();

	Consumer<String> megaphone();

}
