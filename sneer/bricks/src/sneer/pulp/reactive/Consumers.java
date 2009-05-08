package sneer.pulp.reactive;

import sneer.brickness.Brick;
import sneer.hardware.cpu.lang.PickyConsumer;

@Brick
public interface Consumers {

	IntegerParser newIntegerParser(PickyConsumer<Integer> consumer);

	IntegerConsumerBoundaries newIntegerConsumerBoundaries(String friendlyName, PickyConsumer<Integer> endConsumer, int min, int max);
}
