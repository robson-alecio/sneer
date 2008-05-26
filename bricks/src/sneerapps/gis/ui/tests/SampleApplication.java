package sneerapps.gis.ui.tests;

import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneerapps.gis.ui.LocationGui;
import wheel.lang.Threads;

public class SampleApplication  {

	public static void main(String[] args) throws Exception {
		Container container = ContainerUtils.getContainer();
		container.produce(LocationGui.class);
		Threads.sleepWithoutInterruptions(20000);
	}
}
