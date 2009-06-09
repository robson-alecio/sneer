package sneer.bricks.hardware.cpu.utils.consumers.validators.bounds.integer;

import sneer.bricks.hardware.cpu.lang.PickyConsumer;
import sneer.foundation.brickness.Brick;

@Brick
public interface IntegerBounds {

	PickyConsumer<Integer> newInstance(String friendlyName, PickyConsumer<Integer> delegate, int min, int max);

}
