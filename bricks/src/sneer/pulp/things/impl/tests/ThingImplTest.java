package sneer.pulp.things.impl.tests;

import sneer.pulp.things.ThingHome;
import sneer.pulp.things.impl.ThingHomeImpl;
import sneer.pulp.things.tests.ThingsTest;

public class ThingImplTest extends ThingsTest {

	@Override
	protected ThingHome prepareSubject() {
		return new ThingHomeImpl();
	}

}
