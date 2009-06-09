package snapps.wind;

import sneer.brickness.Brick;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.reactive.collections.ListSignal;

@Brick
public interface Wind {

	ListSignal<Shout> shoutsHeard();

	Consumer<String> megaphone();

}
