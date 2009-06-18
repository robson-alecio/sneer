package sneer.bricks.hardware.cpu.utils.consumers.parsers.integer.impl;

import sneer.foundation.lang.PickyConsumer;
import sneer.foundation.lang.exceptions.Refusal;

class IntegerParserImpl implements PickyConsumer<String> {

	private PickyConsumer<Integer> _endConsumer;

	IntegerParserImpl(PickyConsumer<Integer> endConsumer) {
		_endConsumer = endConsumer;
	}

	@Override
	public void consume(String string) throws Refusal {
		Integer result;

		try {
			result = Integer.valueOf(string);

		} catch (NumberFormatException e) {
			throw new Refusal(string + " is not a valid number.");
		}

		_endConsumer.consume(result);
	}
}
