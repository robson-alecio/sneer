package sneer.bricks.hardware.cpu.utils.consumers.validators.bounds.integer;

import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.PickyConsumer;

@Brick
public interface IntegerBounds {

	PickyConsumer<Integer> newInstance(String friendlyName, PickyConsumer<Integer> delegate, int min, int max);

}
