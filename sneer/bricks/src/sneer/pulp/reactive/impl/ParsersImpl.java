package sneer.pulp.reactive.impl;

import sneer.hardware.cpu.lang.PickyConsumer;
import sneer.pulp.reactive.IntegerParser;
import sneer.pulp.reactive.Parsers;

class ParsersImpl implements Parsers {

	@Override
	public IntegerParser createIntegerParserFor(PickyConsumer<Integer> consumer) {
		return new IntegerParserImpl(consumer);
	}

}
