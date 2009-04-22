package sneer.pulp.reactive;

import sneer.brickness.Brick;
import sneer.hardware.cpu.exceptions.IllegalParameter;
import sneer.hardware.cpu.lang.PickyConsumer;

@Brick
public interface IntegerParser extends PickyConsumer<String> {

	void consume(String string) throws IllegalParameter;

}