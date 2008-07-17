package sneerapps.wind;

import wheel.lang.Omnivore;
import wheel.reactive.sets.SetSignal;

public interface Wind {

	void shout(String phrase);

	Omnivore<Float> minAffinityForHearingShouts();
	SetSignal<Shout> shoutsHeard();

}
