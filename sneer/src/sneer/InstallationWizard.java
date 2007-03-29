package sneer;

import java.io.File;

import wheel.io.ui.User;
import wheel.io.ui.impl.JOptionPaneUser;

public class InstallationWizard {

	
	public InstallationWizard(File sneerDirectory) {
		if (sneerDirectory.exists()) return;
		install(sneerDirectory);
	}


	private void install(File sneerDirectory) {
		approveConditionOtherwiseExit(
				" Welcome to Sneer :)\n\n" +
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

		sneerDirectory.mkdir();
		
		_user.acknowledgeNotification(
				" This is an alpha-testing release for ADVANCED Java users.\n\n" +
				" If you really want to tell your grandchildren you were a Sovereign Computing\n" +
				" pioneer, configure Sneer to run on your system startup and help keep the\n" +
				" sovereign network up and alive, now in its early days:\n" +
				"   Linux: Call \"java -jar Sneer.jar\" from a runlevel script.\n" +
				"   Windows: Add a shortcut to Sneer.jar in \"Start > All Programs > Startup\"."
		);

		_user.choose(
				" Congratulations!\n\n" +
				" You are no longer a slave. You have just\n" +
				" claimed your own share of the internet.",
				"Enjoy"
		);
	}

	
	private final User _user = new JOptionPaneUser("Sneer Intallation Wizard");

	
	private void approveConditionOtherwiseExit(String condition) {
		approveConditionOtherwiseExit(condition, "Next >", "Cancel");
	}

	
	private void approveConditionOtherwiseExit(String condition, Object... options) {
		boolean approved = _user.choose(condition, options);
		if (!approved) exit();
	}

	private void exit() {
		String message = " This wizard will now exit with no changes to your system.";
		_user.choose(message, "Exit");
		System.exit(0);
	}


}
