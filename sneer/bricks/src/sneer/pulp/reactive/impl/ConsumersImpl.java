package sneer.pulp.reactive.impl;

import sneer.hardware.cpu.lang.PickyConsumer;
import sneer.pulp.reactive.IntegerConsumerBoundaries;
import sneer.pulp.reactive.IntegerParser;
import sneer.pulp.reactive.Consumers;

class ConsumersImpl implements Consumers {

	@Override
	public IntegerParser newIntegerParser(PickyConsumer<Integer> consumer) {
		return new IntegerParserImpl(consumer);
	}

	@Override
	public IntegerConsumerBoundaries newIntegerConsumerBoundaries(String friendlyName, PickyConsumer<Integer> endConsumer, int min, int max) {
		return new IntegerConsumerBoundariesImpl(friendlyName, endConsumer, min, max);
	}
}
