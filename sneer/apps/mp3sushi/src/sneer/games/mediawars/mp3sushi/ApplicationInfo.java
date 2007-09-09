package sneer.games.mediawars.mp3sushi;

import sneer.kernel.appmanager.SovereignApplicationInfo;

public class ApplicationInfo implements SovereignApplicationInfo{

	@Override
	public String defaultName() {
		return "MP3 Sushi";
	}

	@Override
	public int trafficPriority() {
		return 2;
	}

}
