package sneer.pulp.reactive.impl;

import sneer.hardware.cpu.exceptions.IllegalParameter;
import sneer.hardware.cpu.lang.PickyConsumer;
import sneer.pulp.reactive.IntegerConsumerBoundaries;

class IntegerConsumerBoundariesImpl implements IntegerConsumerBoundaries {

	private final PickyConsumer<Integer> _endConsumer;

	private final int _min;
	private final int _max;
	private final String _friendlyName;

	IntegerConsumerBoundariesImpl(String friendlyName, PickyConsumer<Integer> endConsumer, int min, int max) {
		_friendlyName = friendlyName;
		_endConsumer = endConsumer;
		_min = min;
		_max = max;
	}

	@Override
	public void consume(Integer value) throws IllegalParameter {
		if (value < _min || value > _max) throw new IllegalParameter("" + _friendlyName + " must be no less than " + _min + " and no more than " + _max + ".");
		_endConsumer.consume(value);
	}

}
