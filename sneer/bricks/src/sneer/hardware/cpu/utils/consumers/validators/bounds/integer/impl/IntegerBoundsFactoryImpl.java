package sneer.hardware.cpu.utils.consumers.validators.bounds.integer.impl;

import sneer.hardware.cpu.lang.PickyConsumer;
import sneer.hardware.cpu.utils.consumers.validators.bounds.integer.IntegerBoundsFactory;

class IntegerBoundsFactoryImpl implements IntegerBoundsFactory {

	@Override
	public PickyConsumer<Integer> newIntegerBounds(String friendlyName, PickyConsumer<Integer> endConsumer, int min, int max) {
		return new IntegerBoundsImpl(friendlyName, endConsumer, min, max);
	}
}
