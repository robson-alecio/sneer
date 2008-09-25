package snapps.wind;

import wheel.lang.Omnivore;
import wheel.reactive.lists.ListSignal;

public interface Wind {

	ListSignal<Shout> shoutsHeard();

	Omnivore<String> megaphone();

}
