package sneer.pulp.things.impl.tests;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.things.ThingHome;
import sneer.pulp.things.tests.ThingsTestBase;

public class ThingImplTest extends ThingsTestBase {

	@Override
	protected ThingHome prepareSubject() {
		Container container = ContainerUtils.getContainer();
		return container.produce(ThingHome.class);
	}

}
