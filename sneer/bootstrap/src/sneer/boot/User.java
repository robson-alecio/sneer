package sneer.boot;

import javax.swing.JOptionPane;

public class User {

	public boolean choose(String proposition, Object... options) {
		int chosen = JOptionPane.showOptionDialog(null, proposition + "\n\n", "Sneer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		return chosen == 0;
	}


	String homeDirectory() {
		return System.getProperty("user.home");
	}


	void acknowledgeUnexpectedProblem(String description) {
		JOptionPane.showMessageDialog(null, description, "Sneer - Unexpected Problem", JOptionPane.ERROR_MESSAGE);
	}
	
	public String answer(String prompt) {
		return JOptionPane.showInputDialog(prompt);
	}

}
