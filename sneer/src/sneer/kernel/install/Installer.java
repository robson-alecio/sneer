package sneer.kernel.install;

import static sneer.kernel.SneerDirectories.sneerDirectory;
import wheel.io.ui.User;

public class Installer {

	private final User _user;


	public Installer(User user) throws Exception {
		_user = user;

		if (!sneerDirectory().exists()) tryToInstall();
		if (!sneerDirectory().exists()) return;
	}


	private void tryToInstall() {
		new InstallationDialog(_user);
		sneerDirectory().mkdir();
	}

}
