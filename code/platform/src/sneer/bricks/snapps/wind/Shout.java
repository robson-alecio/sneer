package sneer.bricks.snapps.wind;

import sneer.foundation.brickness.Tuple;

public class Shout extends Tuple {

	public final String phrase;

	public Shout(String phrase_) {
		phrase = phrase_;
	}

	@Override
	public String toString() {
		return phrase;
	}

}
