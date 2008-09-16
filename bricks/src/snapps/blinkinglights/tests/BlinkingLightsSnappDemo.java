package snapps.blinkinglights.tests;

import javax.swing.SwingUtilities;

import snapps.blinkinglights.BlinkingLightsSnapp;
import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.blinkinglights.LightType;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.skin.dashboard.Dashboard;

public class BlinkingLightsSnappDemo  {
	
	public static void main(String[] args) throws Exception {

		Container container = ContainerUtils.getContainer();

		container.produce(Dashboard.class);
		BlinkingLights bl = container.produce(BlinkingLights.class);
		
		bl.turnOn(LightType.INFO, "This is a Info!");
		bl.turnOn(LightType.WARN, "This is a Warning!");
		
		try {
			throw new NullPointerException();
		} catch (NullPointerException e) {
			bl.turnOn(LightType.ERROR, "This is a Error!", e);
		}
		
		container.produce(BlinkingLightsSnapp.class);
		
		waitUntilTheGuiThreadStarts();
	}
	
	private static void waitUntilTheGuiThreadStarts() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable(){@Override public void run() {}});
	}

}