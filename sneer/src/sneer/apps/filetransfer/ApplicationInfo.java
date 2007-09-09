package sneer.apps.filetransfer;

import sneer.kernel.appmanager.SovereignApplicationInfo;

public class ApplicationInfo implements SovereignApplicationInfo{

	@Override
	public String defaultName() {
		return "FileTransfer";
	}

	@Override
	public int trafficPriority() {
		return 0;
	}

}
