package tests.tests.foo;

import sneer.kernel.container.Brick;
import tests.tests.bar.BarBrick;

public interface FooBrick extends Brick {

	BarBrick bar();

}
