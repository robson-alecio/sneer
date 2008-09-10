package snapps.blinkinglights.tests;

import snapps.blinkinglights.BlinkingLightsSnapp;
import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.skin.dashboard.Dashboard;

public class BlinkingLightsSnappDemo  {
	
	public static void main(String[] args) throws Exception {

		Container container = ContainerUtils.getContainer();

		container.produce(Dashboard.class);
		BlinkingLights bl = container.produce(BlinkingLights.class);
		
		bl.turnOn("This is a Warning!");
		
		try {
			String test = null;
			test.toString();
		} catch (NullPointerException e) {
			bl.turnOn("This is a Error!",e);
		}
		
		container.produce(BlinkingLightsSnapp.class);
	}
}