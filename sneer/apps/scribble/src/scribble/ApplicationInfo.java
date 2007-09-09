package scribble;

import sneer.kernel.appmanager.SovereignApplicationInfo;

public class ApplicationInfo implements SovereignApplicationInfo{

	@Override
	public String defaultName() {
		return "Scribble";
	}

	@Override
	public int trafficPriority() {
		return 2;
	}

}
