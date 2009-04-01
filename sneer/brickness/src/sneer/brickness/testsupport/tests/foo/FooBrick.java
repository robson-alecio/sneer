package sneer.brickness.testsupport.tests.foo;

import sneer.brickness.OldBrick;
import sneer.brickness.testsupport.tests.bar.BarBrick;

public interface FooBrick extends OldBrick {

	BarBrick bar();

}
