package sneer.platform;

import static sneer.platform.SneerDirectories.validNumber;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

import sneer.platform.strap.VersionUpdateAttempt;
import wheel.io.Log;
import wheel.io.ui.User;
import wheel.io.ui.User.Action;


public class Platform {


	public Platform() {
		try {
			tryToRun();
		} catch (Throwable t) {
			Log.log(t);
		}
	}

	private void tryToRun() throws Exception {		
	}
}
