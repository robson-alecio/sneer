package snapps.blinkinglights.tests;

import javax.swing.SwingUtilities;

import snapps.blinkinglights.gui.BlinkingLightsGui;
import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.blinkinglights.LightType;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.clockticker.ClockTicker;
import sneer.skin.dashboard.Dashboard;
import wheel.io.Logger;
import wheel.lang.Daemon;
import wheel.lang.Threads;

public class BlinkingLightsDemo  {
	
	public static void main(String[] args) throws Exception {
		Logger.redirectTo(System.out);
		
		Container container = ContainerUtils.getContainer();

		container.produce(Dashboard.class);
		container.produce(ClockTicker.class);
		final BlinkingLights bl = container.produce(BlinkingLights.class);
		
		new Daemon("Blinker") { @Override public void run() {
			while (true) {
				Threads.sleepWithoutInterruptions(700);
				bl.turnOn(LightType.GOOD_NEWS, "GoodNews " + System.currentTimeMillis(), 2000);
			}
		}};
		
		bl.turnOn(LightType.INFO, "Info - expires in 7000ms", 7000);
		bl.turnOn(LightType.WARN, "Warning - expires in 10000ms", 10000);
		
		try {
			throw new NullPointerException();
		} catch (NullPointerException e) {
			bl.turnOn(LightType.ERROR, "This is an Error!", e);
		}
		
		container.produce(BlinkingLightsGui.class);
		
		waitUntilTheGuiThreadStarts();
	}
	
	private static void waitUntilTheGuiThreadStarts() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable(){@Override public void run() {}});
	}

}