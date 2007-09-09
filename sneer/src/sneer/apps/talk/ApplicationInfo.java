package sneer.apps.talk;

import sneer.kernel.appmanager.SovereignApplicationInfo;

public class ApplicationInfo implements SovereignApplicationInfo{

	@Override
	public String defaultName() {
		return "Voice";
	}

	@Override
	public int trafficPriority() {
		return 0;
	}

}
