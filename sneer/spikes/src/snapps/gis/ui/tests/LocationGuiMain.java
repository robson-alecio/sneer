package snapps.gis.ui.tests;

import static sneer.commons.environments.Environments.my;
import snapps.gis.ui.LocationGui;
import sneer.brickness.testsupport.SystemBrickEnvironment;
import sneer.commons.environments.Environments;
import sneer.pulp.threads.Threads;

public class LocationGuiMain  {

	public static void main(String[] args) throws Exception {
		Environments.runWith(new SystemBrickEnvironment(), new Runnable() { @Override public void run() { 
			my(LocationGui.class);
			my(Threads.class).sleepWithoutInterruptions(40000);
		}});
	}
}
