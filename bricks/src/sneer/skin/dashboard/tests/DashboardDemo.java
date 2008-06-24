package sneer.skin.dashboard.tests;

import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.skin.dashboard.Dashboard;
import wheel.lang.Threads;

public class DashboardDemo  {

	public static void main(String[] args) throws Exception {
		Container container = ContainerUtils.getContainer();

		container.produce(Dashboard.class);
		
		Threads.sleepWithoutInterruptions(30000);
	}
}
