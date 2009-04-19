package snapps.gis.ui.tests;

import static sneer.commons.environments.Environments.my;
import snapps.gis.ui.LocationGui;
import sneer.brickness.testsupport.SystemBrickEnvironment;
import sneer.commons.environments.Environments;
import wheel.lang.Threads;

public class LocationGuiMain  {

	public static void main(String[] args) throws Exception {
		Environments.runWith(new SystemBrickEnvironment(), new Runnable() { @Override public void run() { 
			my(LocationGui.class);
			Threads.sleepWithoutInterruptions(40000);
		}});
	}
}
