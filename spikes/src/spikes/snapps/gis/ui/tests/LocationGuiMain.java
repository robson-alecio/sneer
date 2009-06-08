package spikes.snapps.gis.ui.tests;

import sneer.brickness.Brickness;
import sneer.commons.environments.Environment;
import sneer.commons.environments.EnvironmentUtils;
import sneer.pulp.threads.Threads;
import spikes.snapps.gis.ui.LocationGui;

public class LocationGuiMain  {

	public static void main(String[] args) throws Exception {
		Environment container = Brickness.newBrickContainer();
		EnvironmentUtils.retrieveFrom(container, LocationGui.class);
		EnvironmentUtils.retrieveFrom(container, Threads.class).sleepWithoutInterruptions(40000);
	}
}