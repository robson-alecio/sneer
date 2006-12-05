package spikes.vitor.security;

import javax.swing.JFrame;

public class BadImportPlugin {

	public static void run() {
		System.out.println("Importing Swing.");
		new JFrame().setVisible(true);
		System.out.println("Importing Swing. This line couldn't be printed");
	}

}
