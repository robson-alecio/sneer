package spikes.snapps.gis.ui.tests;

import sneer.bricks.pulp.threads.Threads;
import sneer.foundation.brickness.Brickness;
import sneer.foundation.commons.environments.Environment;
import sneer.foundation.commons.environments.EnvironmentUtils;
import spikes.snapps.gis.ui.LocationGui;

public class LocationGuiMain  {

	public static void main(String[] args) throws Exception {
		Environment container = Brickness.newBrickContainer();
		EnvironmentUtils.retrieveFrom(container, LocationGui.class);
		EnvironmentUtils.retrieveFrom(container, Threads.class).sleepWithoutInterruptions(40000);
	}
}