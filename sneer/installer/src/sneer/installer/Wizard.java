package sneer.installer;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class Wizard extends JFrame{

	private final String WIZARD_TITLE = "Sneer Installation Wizard";
	private final File _sneerHome;

	Wizard(File sneerHome) throws Exception {
		_sneerHome = sneerHome;
		if (sneerHome.exists()) return;

		useNimbus();
		doWizard();
		useMetal();
	}

	private void doWizard() throws IOException {
		welcome();
		license();
		dogFoodInformation();
		configInformation();
		
		new Installer(_sneerHome);
		
		congratulations();
	}

	private void welcome() {
		showDialog(
		"Welcome to Sneer, the first sovereign computing peer.  :)\n\n" +
		"This wizard will prepare Sneer to run for you.", 
		
		"Whatever >"); 
	}

	private void license() {
		showDialog(
		"Sneer is free software.\n\n" +
		"It is licensed under the terms of the GNU Affero General Public License\n" +
		"version 3 as published by the Free Software Foundation:\n" +
		"http://www.fsf.org/licensing/licenses/agpl-3.0.html\n\n" +
		"Do you accept these terms?", 
		
		"I Accept >","I Do Not Accept"); 		
	}
	
	private void dogFoodInformation() {
		showDialog(
		"This is the Sneer 'Dogfood Release' for ADVANCED Java users.\n\n" +
		"Please do not install Sneer for regular users yet.", 
		
		"Whatever >"); 
	}
	
	private void configInformation() {
		showDialog(
		"Each user of this computer can have his own Sneer setup.\n\n" +
		"To store your setup, the following folder will be created:\n" +
		_sneerHome.getAbsolutePath(), 
		
		"Whatever >");
	}
	
	private void congratulations() {
		Dialogs.show(WIZARD_TITLE,
		"Congratulations!\n\n" +
		"You are no longer a slave. You have just\n" +
		"claimed your own share of the internet.", systemExit(),
				
		"Start Sneer");
	}

	private void useMetal() {
		try {
			UIManager.setLookAndFeel(new MetalLookAndFeel());
		} catch (Exception ignore) {}
	}
	
	private void useNimbus() {
		try {
			UIManager.setLookAndFeel((LookAndFeel) Class.forName("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel").newInstance());
		} catch (Exception ignore) {}
	}
	
	private void showDialog(String msg, Object...options) {
		Dialogs.show(WIZARD_TITLE, msg,	exitDialog(), options);
	}

	private Runnable exitDialog() {
		return new Runnable() { @Override public void run() {
			Dialogs.show(WIZARD_TITLE, "This wizard will now exit with no changes to your system.", systemExit(), "Exit");
		}};
	}

	private Runnable systemExit() {
		return new Runnable() { @Override public void run() {
			System.exit(0);
		}};
	}

}