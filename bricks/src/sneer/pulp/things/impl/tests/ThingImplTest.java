package sneer.pulp.things.impl.tests;

import sneer.pulp.things.ThingHome;
import sneer.pulp.things.impl.ThingHomeImpl;
import sneer.pulp.things.tests.ThingsTestBase;

public class ThingImplTest extends ThingsTestBase {

	@Override
	protected ThingHome prepareSubject() {
		return new ThingHomeImpl();
	}

}
