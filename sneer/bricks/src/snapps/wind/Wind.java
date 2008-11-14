package snapps.wind;

import wheel.lang.Consumer;
import wheel.reactive.lists.ListSignal;

public interface Wind {

	ListSignal<Shout> shoutsHeard();

	Consumer<String> megaphone();

}
