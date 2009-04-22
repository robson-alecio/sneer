package sneer.pulp.reactive.impl;

import sneer.hardware.cpu.exceptions.IllegalParameter;
import sneer.hardware.cpu.lang.PickyConsumer;
import sneer.pulp.reactive.IntegerParser;

class IntegerParserImpl implements IntegerParser {

	private PickyConsumer<Integer> _endConsumer;

	IntegerParserImpl(PickyConsumer<Integer> endConsumer) {
		_endConsumer = endConsumer;
	}

	@Override
	public void consume(String string) throws IllegalParameter {
		Integer result;

		try {
			result = Integer.valueOf(string);

		} catch (NumberFormatException e) {
			throw new IllegalParameter(string + " is not a valid number.");
		}

		_endConsumer.consume(result);
	}
}
