package snapps.gis.ui.tests;

import static sneer.commons.environments.Environments.my;
import snapps.gis.ui.LocationGui;
import sneer.commons.environments.Environments;
import sneer.kernel.container.ContainersOld;
import wheel.lang.Threads;

public class LocationGuiMain  {

	public static void main(String[] args) throws Exception {
		Environments.runWith(ContainersOld.newContainer(), new Runnable() { @Override public void run() { 
			my(LocationGui.class);
			Threads.sleepWithoutInterruptions(40000);
		}});
	}
}
