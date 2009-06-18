package sneer.bricks.hardware.cpu.utils.consumers.parsers.integer;

import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.PickyConsumer;

@Brick
public interface IntegerParsers {

	PickyConsumer<String> newIntegerParser(PickyConsumer<Integer> delegate);

}
