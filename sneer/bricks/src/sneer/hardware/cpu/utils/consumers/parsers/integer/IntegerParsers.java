package sneer.hardware.cpu.utils.consumers.parsers.integer;

import sneer.brickness.Brick;
import sneer.hardware.cpu.lang.PickyConsumer;

@Brick
public interface IntegerParsers {

	PickyConsumer<String> newIntegerParser(PickyConsumer<Integer> delegate);

}
