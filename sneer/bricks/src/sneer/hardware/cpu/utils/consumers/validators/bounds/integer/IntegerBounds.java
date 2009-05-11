package sneer.hardware.cpu.utils.consumers.validators.bounds.integer;

import sneer.brickness.Brick;
import sneer.hardware.cpu.lang.PickyConsumer;

@Brick
public interface IntegerBounds {

	PickyConsumer<Integer> newInstance(String friendlyName, PickyConsumer<Integer> delegate, int min, int max);

}
