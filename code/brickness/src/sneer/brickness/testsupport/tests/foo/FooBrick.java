package sneer.brickness.testsupport.tests.foo;

import sneer.brickness.Brick;
import sneer.brickness.testsupport.tests.bar.BarBrick;

@Brick
public interface FooBrick {

	BarBrick bar();

}
