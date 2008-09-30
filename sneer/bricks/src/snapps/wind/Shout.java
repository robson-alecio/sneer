package snapps.wind;

import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.tuples.Tuple;

public class Shout extends Tuple {

	public final String phrase;

	public Shout(PublicKey pPublisher, long pPublicationTime, String pPhrase) {
		super(pPublisher, pPublicationTime);
		phrase = pPhrase;
	}

	@Override
	public String toString() {
		return phrase;
	}

}
