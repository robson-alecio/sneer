package sneer.bricks.pulp.log.filter;

import sneer.foundation.brickness.Tuple;

public class LogWhiteListEntry extends Tuple {

	public final String phrase;

	public LogWhiteListEntry(String phrase_) {
		phrase = phrase_;
	}	

}
