package sneerapps.wind;

import wheel.reactive.sets.SetSignal;

public interface Wind {

	void shout(String phrase);

	SetSignal<Shout> shoutsHeard();

}
