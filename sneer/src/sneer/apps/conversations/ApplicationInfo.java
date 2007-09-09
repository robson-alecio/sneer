package sneer.apps.conversations;

import sneer.kernel.appmanager.SovereignApplicationInfo;

public class ApplicationInfo implements SovereignApplicationInfo{

	@Override
	public String defaultName() {
		return "Messages";
	}

	@Override
	public int trafficPriority() {
		return 0;
	}

}
