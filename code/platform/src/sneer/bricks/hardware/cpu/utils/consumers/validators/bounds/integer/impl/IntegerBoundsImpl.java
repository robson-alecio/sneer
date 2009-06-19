package sneer.bricks.hardware.cpu.utils.consumers.validators.bounds.integer.impl;

import sneer.bricks.hardware.cpu.utils.consumers.validators.bounds.integer.IntegerBounds;
import sneer.foundation.lang.PickyConsumer;
import sneer.foundation.lang.exceptions.Refusal;

class IntegerBoundsImpl implements IntegerBounds {

	@Override
	public PickyConsumer<Integer> newInstance(String friendlyName, PickyConsumer<Integer> endConsumer, int min, int max) {
		return new IntegerBoundsInstance(friendlyName, endConsumer, min, max);
	}

	private class IntegerBoundsInstance implements PickyConsumer<Integer> {

		private final PickyConsumer<Integer> _endConsumer;

		private final int _min;
		private final int _max;
		private final String _friendlyName;

		IntegerBoundsInstance(String friendlyName, PickyConsumer<Integer> endConsumer, int min, int max) {
			_friendlyName = friendlyName;
			_endConsumer = endConsumer;
			_min = min;
			_max = max;
		}

		@Override
		public void consume(Integer value) throws Refusal {
			if (value < _min || value > _max) throw new Refusal("" + _friendlyName + " must be no less than " + _min + " and no more than " + _max + ".");
			_endConsumer.consume(value);
		}

	}
}
