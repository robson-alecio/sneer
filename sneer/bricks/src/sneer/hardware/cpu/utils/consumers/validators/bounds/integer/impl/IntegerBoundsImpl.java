package sneer.hardware.cpu.utils.consumers.validators.bounds.integer.impl;

import sneer.hardware.cpu.lang.PickyConsumer;
import sneer.hardware.cpu.utils.consumers.validators.bounds.integer.IntegerBounds;

class IntegerBoundsImpl implements IntegerBounds {

	@Override
	public PickyConsumer<Integer> newInstance(String friendlyName, PickyConsumer<Integer> endConsumer, int min, int max) {
		return new IntegerBoundsInstance(friendlyName, endConsumer, min, max);
	}
}
