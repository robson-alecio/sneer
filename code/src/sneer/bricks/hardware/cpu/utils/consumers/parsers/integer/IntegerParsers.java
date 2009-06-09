package sneer.bricks.hardware.cpu.utils.consumers.parsers.integer;

import sneer.bricks.hardware.cpu.lang.PickyConsumer;
import sneer.foundation.brickness.Brick;

@Brick
public interface IntegerParsers {

	PickyConsumer<String> newIntegerParser(PickyConsumer<Integer> delegate);

}
