package sneer.brickness.testsupport.tests.foo;

import sneer.brickness.testsupport.tests.bar.BarBrick;
import sneer.kernel.container.Brick;

public interface FooBrick extends Brick {

	BarBrick bar();

}
