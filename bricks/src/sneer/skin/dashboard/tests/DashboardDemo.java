package sneer.skin.dashboard.tests;

import sneer.lego.Container;
import sneer.lego.ContainerUtils;
import sneer.skin.dashboard.Dashboard;
import sneer.skin.laf.LafManager;
import sneer.skin.laf.metal.MetalLafSupport;
import sneer.skin.laf.motif.MotifLafSupport;
import sneer.skin.laf.napkin.NapkinLafSupport;
import sneer.skin.laf.so.SOLafSupport;
import sneer.skin.laf.sustance.SustanceLafSupport;
import wheel.lang.Threads;

public class DashboardDemo  {

	public static void main(String[] args) throws Exception {
		Container container = ContainerUtils.getContainer();

		container.produce(Dashboard.class);
		container.produce(SOLafSupport.class);
		container.produce(MetalLafSupport.class);
		container.produce(MotifLafSupport.class);
		container.produce(SustanceLafSupport.class);
		
		NapkinLafSupport tmp = container.produce(NapkinLafSupport.class);
		
		LafManager reg = container.produce(LafManager.class);
		reg.setActiveLafSupport(tmp);
		
		Threads.sleepWithoutInterruptions(30000);
	}
}