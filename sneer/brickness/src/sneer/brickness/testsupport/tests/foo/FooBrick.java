package sneer.brickness.testsupport.tests.foo;

import sneer.brickness.Brick;
import sneer.brickness.testsupport.tests.bar.BarBrick;

public interface FooBrick extends Brick {

	BarBrick bar();

}
