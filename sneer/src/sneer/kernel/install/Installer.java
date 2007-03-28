package sneer.kernel.install;

import static sneer.kernel.SneerDirectories.sneerDirectory;
import wheel.io.ui.User;

public class Installer {

	public Installer(User user) throws Exception {
		if (sneerDirectory().exists()) return;

		new InstallationDialog(user);
		sneerDirectory().mkdir();
	}

}
