package sneer;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import static sneer.Language.*;
import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;
import wheel.io.ui.impl.JOptionPaneUser;

public class InstallationWizard {

	
	public InstallationWizard(File sneerDirectory) throws IOException {
		if (sneerDirectory.exists()) return;
		install(sneerDirectory);
	}


	private void install(File sneerDirectory) throws IOException {
		defineLanguage();
		
		approveConditionOtherwiseExit(string("INSTALLWIZARD_WELCOME"));

		approveConditionOtherwiseExit(string("INSTALLWIZARD_LICENSE"), string("INSTALLWIZARD_LICENSE_ACCEPT"), string("INSTALLWIZARD_LICENSE_NO"));

		approveConditionOtherwiseExit(string("INSTALLWIZARD_FOLDER_CREATION", sneerDirectory));

		if (!sneerDirectory.mkdir()) throw new IOException("Unable to create folder " + sneerDirectory);
		
		_user.acknowledgeNotification(string("INSTALLWIZARD_STARTUP"));

		_user.acknowledgeNotification(string("INSTALLWIZARD_CONGRATULATIONS"),string("INSTALLWIZARD_CONGRATULATIONS_ENJOY"));
	}


	private void defineLanguage() {
		try {
			String language = (String)_user.choose("Choose a language:", "English", "Português");
			if (language.equals("English")){
				changeLocale(new Locale("en"));
			}else{
				changeLocale(new Locale("pt","BR"));
			}
		} catch (CancelledByUser e) {
			exit();
		}
	}

	
	private final User _user = new JOptionPaneUser("Sneer Intallation Wizard");

	
	private void approveConditionOtherwiseExit(String condition) {
		approveConditionOtherwiseExit(condition, string("INSTALLWIZARD_NEXT"), string("INSTALLWIZARD_CANCEL"));
	}

	
	private void approveConditionOtherwiseExit(String condition, Object... options) {
		try {
			Object approved = _user.choose(condition, options);
			if (approved != options[0]) exit();
		} catch (CancelledByUser e) {
			exit();
		}
	}

	private void exit() {
		try {
			_user.choose(string("INSTALLWIZARD_EXIT_NO_CHANGES"), string("INSTALLWIZARD_EXIT"));
		} catch (CancelledByUser e) {}
		System.exit(0);
	}


}
