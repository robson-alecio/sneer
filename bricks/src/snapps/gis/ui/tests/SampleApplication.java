package snapps.gis.ui.tests;

import snapps.gis.ui.LocationGui;
import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import wheel.lang.Threads;

public class SampleApplication  {

	public static void main(String[] args) throws Exception {
		Container container = ContainerUtils.getContainer();
		container.produce(LocationGui.class);
		Threads.sleepWithoutInterruptions(40000);
	}
}
