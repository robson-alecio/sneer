package sneer.skin.dashboard.tests;

import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.skin.dashboard.Dashboard;
import sneer.skin.laf.metal.MetalLafSupport;
import sneer.skin.laf.motif.MotifLafSupport;
import sneer.skin.laf.napkin.NapkinLafSupport;
import sneer.skin.laf.so.SOLafSupport;
import sneer.skin.sustance.SustanceLafSupport;
import wheel.lang.Threads;

public class DashboardDemo  {

	public static void main(String[] args) throws Exception {
		Container container = ContainerUtils.getContainer();

		container.produce(Dashboard.class);
		container.produce(SOLafSupport.class);
		container.produce(MetalLafSupport.class);
		container.produce(MotifLafSupport.class);
		container.produce(SustanceLafSupport.class);
		container.produce(NapkinLafSupport.class).getAction().run();
		
		Threads.sleepWithoutInterruptions(30000);
	}
}