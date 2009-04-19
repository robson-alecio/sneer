package spikes.demos;

import static sneer.commons.environments.Environments.my;
import snapps.blinkinglights.gui.BlinkingLightsGui;
import sneer.brickness.testsupport.SystemBrickEnvironment;
import sneer.commons.environments.Environments;
import sneer.pulp.blinkinglights.BlinkingLights;
import sneer.pulp.blinkinglights.LightType;
import sneer.pulp.logging.out.LogToSystemOut;
import sneer.skin.main.dashboard.Dashboard;

public class BlinkingLightsDemo {

	BlinkingLightsDemo() throws Exception {
		my(LogToSystemOut.class);

		my(Dashboard.class);
		my(BlinkingLightsGui.class);
		BlinkingLights bl = my(BlinkingLights.class);
		
		bl.turnOn(LightType.INFO, "Info", "Info - expires in 7000ms", 7000);
		bl.turnOn(LightType.WARN, "Warning", "Warning - expires in 10000ms", 10000);
		bl.turnOn(LightType.ERROR, "Error", "This is an Error!", new NullPointerException());
	}

	public static void main(String[] args) throws Exception {
		my(LogToSystemOut.class);
		Environments.runWith(new SystemBrickEnvironment(), new Runnable(){
			@Override public void run() {
				try {
					new BlinkingLightsDemo();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}});
	}
}