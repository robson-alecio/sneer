package sneer.bricks.things.impl.tests;

import sneer.bricks.things.ThingHome;
import sneer.bricks.things.impl.ThingHomeImpl;
import sneer.bricks.things.tests.ThingsTest;

public class ThingImplTest extends ThingsTest {

	@Override
	protected ThingHome prepareSubject() {
		return new ThingHomeImpl();
	}

}
