package sneer;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import static wheel.i18n.Language.*;
import wheel.i18n.Language;
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
		
		approveConditionOtherwiseExit(translate(
				"Welcome to Sneer, the first sovereign computing peer.  :)\n\n" +
				"This wizard will prepare Sneer to run for you."
		));

		approveConditionOtherwiseExit(translate(
				"Sneer is free software.\n\n" +
				"It is licensed under the terms of the General Public License version 2\n" +
				"as published by the Free Software Foundation:\n" +
				"http://www.gnu.org/copyleft/gpl.html\n\n" +
				"Do you accept these terms?"),
				translate("I Accept >"),
				translate("No")
		);

		approveConditionOtherwiseExit(translate(
				"Each user of this computer can have his own Sneer setup.\n\n" +
				"To store your setup, the following folder will be created:\n" +
				"%1$s",sneerDirectory)
		);

		if (!sneerDirectory.mkdir()) throw new IOException("Unable to create folder " + sneerDirectory);
		
		_user.acknowledgeNotification(translate(
				"This is an alpha-testing release for ADVANCED Java users.\n\n" +
				"Please configure Sneer to run on your system startup and help keep the\n" +
				"sovereign network up and alive, now in its early days:\n" +
				"   Linux: Call \"java -jar Sneer.jar\" from a runlevel script.\n" +
				"   Windows: Add a shortcut to Sneer.jar in \"Start > All Programs > Startup\"."
		));

		_user.acknowledgeNotification(translate(
				"Congratulations!\n\n" +
				"You are no longer a slave. You have just\n" +
				"claimed your own share of the internet."),
				translate("Enjoy")
		);
	}


	private void defineLanguage() {
		try {
			String language = (String)_user.choose("Choose a language:", "English", "Português");
			if (language.equals("English")){
				reset();
			}else{
				load("pt_BR");
			}
		} catch (CancelledByUser e) {
			exit();
		}
	}

	
	private final User _user = new JOptionPaneUser("Sneer Intallation Wizard");

	
	private void approveConditionOtherwiseExit(String condition) {
		approveConditionOtherwiseExit(condition, translate("Next >"), translate("Cancel"));
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
		String message = translate("This wizard will now exit with no changes to your system.");
		try {
			_user.choose(message, translate("Exit"));
		} catch (CancelledByUser e) {}
		System.exit(0);
	}


}
