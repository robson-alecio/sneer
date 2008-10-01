package snapps.wind;

import sneer.pulp.tuples.Tuple;

public class Shout extends Tuple {

	public final String phrase;

	public Shout(String pPhrase) {
		phrase = pPhrase;
	}

	@Override
	public String toString() {
		return phrase;
	}

}
