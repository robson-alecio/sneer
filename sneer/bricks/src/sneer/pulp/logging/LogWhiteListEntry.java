package sneer.pulp.logging;

import sneer.kernel.container.Tuple;

public class LogWhiteListEntry extends Tuple {

	public final String phrase;

	public LogWhiteListEntry(String phrase_) {
		phrase = phrase_;
	}	

}
