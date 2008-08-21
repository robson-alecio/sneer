package snapps.gis.ui.tests;

import snapps.gis.ui.LocationGui;
import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import wheel.lang.Threads;

public class SampleApplication  {

	public static void main(String[] args) throws Exception {
		Container container = ContainerUtils.getContainer();
		container.produce(LocationGui.class);
		Threads.sleepWithoutInterruptions(40000);
	}
}
