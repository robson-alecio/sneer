package wheel.io.ui.impl;

import javax.swing.JOptionPane;

import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;

public class JOptionPaneUser implements User {
	
	public JOptionPaneUser(String title) {
		_title = title;
	}

	
	private final String _title;

	
	public Object choose(String proposition, Object... options) throws CancelledByUser {
		int chosen = JOptionPane.showOptionDialog(null, proposition + "\n\n", _title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (chosen == -1) throw new CancelledByUser();
		return options[chosen];
	}


	public void acknowledgeUnexpectedProblem(String description) {
		JOptionPane.showMessageDialog(null, description + "\n\n", _title + " - Unexpected Problem", JOptionPane.ERROR_MESSAGE);
	}

	
	public String answer(String prompt) throws CancelledByUser {
		return answer(prompt, "");
	}

	
	public String answer(String prompt, String defaultAnswer) throws CancelledByUser {
		String answer = JOptionPane.showInputDialog(prompt + "\n\n", defaultAnswer);
		if (answer == null) throw new CancelledByUser();
		return answer;
	}

	
	public void acknowledgeNotification(String notification) {
		acknowledgeNotification(notification, "OK");
	}


	public void acknowledgeNotification(String notification, String acknowledgement) {
		try {
			choose(notification, acknowledgement);
		} catch (CancelledByUser e) {}
	}


	public int answerWithNumber(String prompt) throws CancelledByUser {
		String fullPrompt = prompt;
		while (true) {
			String answer = answer(fullPrompt);
			try {
				return Integer.parseInt(answer);
			} catch (RuntimeException e) {
				fullPrompt = " Invalid number: " + answer + "\n\n Retry: " + prompt;
			}
		}
	}
}
