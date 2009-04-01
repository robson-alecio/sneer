package snapps.wind;

import sneer.brickness.OldBrick;
import wheel.lang.Consumer;
import wheel.reactive.lists.ListSignal;

public interface Wind extends OldBrick {

	ListSignal<Shout> shoutsHeard();

	Consumer<String> megaphone();

}
