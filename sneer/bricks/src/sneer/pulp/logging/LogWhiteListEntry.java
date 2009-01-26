package sneer.pulp.logging;

import sneer.pulp.tuples.Tuple;

public class LogWhiteListEntry extends Tuple {

	public final String phrase;

	public LogWhiteListEntry(String phrase_) {
		phrase = phrase_;
	}	

}
