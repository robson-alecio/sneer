package sneer.installer;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import main.SneerStoragePath;

public class Wizard extends JFrame{

	private static final String WIZARD_TITLE = "Sneer Installation Wizard";
	private final File _sneerHome;

	public Wizard(File sneerHome) {
		_sneerHome = sneerHome;
		welcome();
		license();
		dogFoodInformation();
		configInformation();
		congratulations();
	}

	public static void main(String[] args) {
		new Wizard(new File(new SneerStoragePath().get()));
	}

	private void welcome() {
		String msg = 
		"Welcome to Sneer, the first sovereign computing peer.  :)\n\n" +
		"This wizard will prepare Sneer to run for you.";
		
		Object[] options = {"Whatever >"};

		JOptionPane.showOptionDialog(null, msg, WIZARD_TITLE, 
			JOptionPane.OK_OPTION,
			JOptionPane.INFORMATION_MESSAGE,
			null,  options,  options[0]); 
	}

	private void license() {
		String msg = 
		"Sneer is free software.\n\n" +
		"It is licensed under the terms of the GNU Affero General Public License\n" +
		"version 3 as published by the Free Software Foundation:\n" +
		"http://www.fsf.org/licensing/licenses/agpl-3.0.html\n\n" +
		"Do you accept these terms?";
		
		Object[] options = {"I Accept >","I Do Not Accept"};
		
		int bnt = JOptionPane.showOptionDialog(null, msg, WIZARD_TITLE,
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE,
				null,  options,  options[0]); 				
		
		if(bnt!=JOptionPane.OK_OPTION)
			System.exit(0);
	}
	

	private void dogFoodInformation() {
		String msg = 
		"This is the Sneer 'Dogfood Release' for ADVANCED Java users.\n\n" +
		"Please do not install Sneer for regular users yet.";
		
		Object[] options = {"Whatever >"};

		JOptionPane.showOptionDialog(null, msg, WIZARD_TITLE, 
			JOptionPane.OK_OPTION,
			JOptionPane.INFORMATION_MESSAGE,
			null,  options,  options[0]); 
	}
	
	private void configInformation() {
		String msg = 
		"Each user of this computer can have his own Sneer setup.\n\n" +
		"To store your setup, the following folder will be created:\n" +
		_sneerHome.getAbsolutePath();
		
		Object[] options = {"Whatever >"};

		JOptionPane.showOptionDialog(null, msg, WIZARD_TITLE, 
			JOptionPane.OK_OPTION,
			JOptionPane.INFORMATION_MESSAGE,
			null,  options,  options[0]); 
	}	
	
	private void congratulations() {
		String msg = 
		"Congratulations!\n\n" +
		"You are no longer a slave. You have just\n" +
		"claimed your own share of the internet.";
		
		Object[] options = {"Start Sneer"};

		JOptionPane.showOptionDialog(null, msg, WIZARD_TITLE, 
			JOptionPane.OK_OPTION,
			JOptionPane.INFORMATION_MESSAGE,
			null,  options,  options[0]); 
	}
}