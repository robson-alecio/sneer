package snapps.gis.ui.tests;

import snapps.gis.ui.LocationGui;
import sneer.brickness.Brickness;
import sneer.commons.environments.Environment;
import sneer.commons.environments.EnvironmentUtils;
import sneer.pulp.threads.Threads;

public class LocationGuiMain  {

	public static void main(String[] args) throws Exception {
		Environment container = Brickness.newBrickContainer();
		EnvironmentUtils.retrieveFrom(container, LocationGui.class);
		EnvironmentUtils.retrieveFrom(container, Threads.class).sleepWithoutInterruptions(40000);
	}
}