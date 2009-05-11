package sneer.hardware.cpu.utils.consumers.parsers.integer.impl;

import sneer.hardware.cpu.lang.PickyConsumer;
import sneer.hardware.cpu.utils.consumers.parsers.integer.IntegerParsers;

class IntegerParsersImpl implements IntegerParsers {

	@Override
	public PickyConsumer<String> newIntegerParser(PickyConsumer<Integer> delegate) {
		return new IntegerParserImpl(delegate);
	}

}
