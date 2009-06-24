package spikes.demos;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.pulp.blinkinglights.BlinkingLights;
import sneer.bricks.pulp.blinkinglights.LightType;
import sneer.bricks.skin.main.dashboard.Dashboard;
import sneer.bricks.snapps.system.blinkinglights.gui.BlinkingLightsGui;
import sneer.foundation.brickness.Brickness;
import sneer.foundation.environments.Environments;

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
		Environments.runWith(Brickness.newBrickContainer(), new Runnable(){
			@Override public void run() {
				try {
					new BlinkingLightsDemo();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}});
	}
}