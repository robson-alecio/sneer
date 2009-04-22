package sneer.pulp.reactive;

import sneer.brickness.Brick;
import sneer.hardware.cpu.lang.PickyConsumer;

@Brick
public interface Parsers {

	IntegerParser createIntegerParserFor(PickyConsumer<Integer> consumer);

}
