package spikes.demos;

import static sneer.commons.environments.Environments.my;
import snapps.blinkinglights.gui.BlinkingLightsGui;
import sneer.commons.environments.Environments;
import sneer.kernel.container.ContainersOld;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.LightType;
import sneer.skin.olddashboard.Dashboard;
import wheel.io.Logger;

public class BlinkingLightsDemo {

	BlinkingLightsDemo() throws Exception {

		my(Dashboard.class);
		my(BlinkingLightsGui.class);
		BlinkingLights bl = my(BlinkingLights.class);
		
		bl.turnOn(LightType.INFO, "Info", "Info - expires in 7000ms", 7000);
		bl.turnOn(LightType.WARN, "Warning", "Warning - expires in 10000ms", 10000);
		bl.turnOn(LightType.ERROR, "Error", "This is an Error!", new NullPointerException());
	}

	public static void main(String[] args) throws Exception {
		Logger.redirectTo(System.out);
		Environments.runWith(ContainersOld.newContainer(), new Runnable(){
			@Override public void run() {
				try {
					new BlinkingLightsDemo();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}});
	}
}