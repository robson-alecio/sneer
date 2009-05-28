package sneer.installer;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

import main.SneerStoragePath;

import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

public class Wizard extends JFrame{

	private final String WIZARD_TITLE = "Sneer Installation Wizard";
	private final SneerStoragePath _sneerStoragePath;

	Wizard(SneerStoragePath sneerStoragePath) throws Exception {
		_sneerStoragePath = sneerStoragePath;
		useNimbus();
		welcome();
		license();
		dogFoodInformation();
		configInformation();
		tryInstall();
		congratulations();
		useMetal();
		startSneer();
	}

	private void showDialog(String msg, Object...options) {
		Dialogs.show(WIZARD_TITLE, msg,	options);
	}

	private void startSneer() throws Exception {
		new SneerJockey(_sneerStoragePath);
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
		_sneerStoragePath.get(), 
		
		"Whatever >");
	}
	
	private void congratulations() {
		Object options[] = new Object[]{"Start Sneer"}; 
		
		int bnt = JOptionPane.showOptionDialog(null, 
				"Congratulations!\n\n" +
				"You are no longer a slave. You have just\n" +
				"claimed your own share of the internet.", 
				
				WIZARD_TITLE,  OK_OPTION, INFORMATION_MESSAGE, null,  options,  options[0]);
		
		if(bnt!=OK_OPTION)
			System.exit(0);
	}

	private void tryInstall() throws IOException {
		new Installer().install(_sneerStoragePath);
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
	
	public static void main(String[] args) throws Exception {
		new Wizard(new SneerStoragePath());
	}
}