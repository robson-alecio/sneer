package sneer.bricks.hardware.cpu.utils.consumers.parsers.integer.impl;

import sneer.bricks.hardware.cpu.lang.PickyConsumer;
import sneer.bricks.hardware.cpu.utils.consumers.parsers.integer.IntegerParsers;

class IntegerParsersImpl implements IntegerParsers {

	@Override
	public PickyConsumer<String> newIntegerParser(PickyConsumer<Integer> delegate) {
		return new IntegerParserImpl(delegate);
	}

}
