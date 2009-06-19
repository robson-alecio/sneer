package sneer.foundation.brickness.testsupport.tests.foo;

import sneer.foundation.brickness.Brick;
import sneer.foundation.brickness.testsupport.tests.bar.BarBrick;

@Brick
public interface FooBrick {

	BarBrick bar();

}
