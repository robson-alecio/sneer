package sneer;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

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
		
		approveConditionOtherwiseExit(
				" Welcome to Sneer, the first sovereign computing peer.  :)\n\n" +
				" This wizard will prepare Sneer to run for you."
		);

		approveConditionOtherwiseExit(
				" Sneer is free software.\n\n" +
				" It is licensed under the terms of the General Public License version 2\n" +
				" as published by the Free Software Foundation:\n" +
				" http://www.gnu.org/copyleft/gpl.html\n\n" +
				" Do you accept these terms?",
				"I Accept >", "No"
		);

		approveConditionOtherwiseExit(
				" Each user of this computer can have his own Sneer setup.\n\n" +
				" To store your setup, the following folder will be created:\n" +
				" " + sneerDirectory
		);

		if (!sneerDirectory.mkdir()) throw new IOException("Unable to create folder " + sneerDirectory);
		
		_user.acknowledgeNotification(
				" This is an alpha-testing release for ADVANCED Java users.\n\n" +
				" Please configure Sneer to run on your system startup and help keep the\n" +
				" sovereign network up and alive, now in its early days:\n" +
				"   Linux: Call \"java -jar Sneer.jar\" from a runlevel script.\n" +
				"   Windows: Add a shortcut to Sneer.jar in \"Start > All Programs > Startup\"."
		);

		_user.acknowledgeNotification(
				" Congratulations!\n\n" +
				" You are no longer a slave. You have just\n" +
				" claimed your own share of the internet.",
				"Enjoy"
		);
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
		approveConditionOtherwiseExit(condition, "Next >", "Cancel");
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
		String message = " This wizard will now exit with no changes to your system.";
		try {
			_user.choose(message, "Exit");
		} catch (CancelledByUser e) {}
		System.exit(0);
	}


}
