package sneer.hardware.cpu.utils.consumers.validators.bounds.integer;

import sneer.brickness.Brick;
import sneer.hardware.cpu.lang.PickyConsumer;

@Brick
public interface IntegerBoundsFactory {

	PickyConsumer<Integer> newIntegerBounds(String friendlyName, PickyConsumer<Integer> delegate, int min, int max);

}
