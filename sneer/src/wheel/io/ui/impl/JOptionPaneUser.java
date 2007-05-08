package wheel.io.ui.impl;

import javax.swing.JOptionPane;

import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;
import wheel.lang.exceptions.Catcher;

public class JOptionPaneUser implements User {
	
	public JOptionPaneUser(String title) { //Fix: receive the parent component instead of passing null to the JOptionPane in order not to lose the focus.
		_title = title;
	}

	
	private final String _title;

	
	public Object choose(String proposition, Object... options) throws CancelledByUser {
		int chosen = JOptionPane.showOptionDialog(null, proposition + "\n\n", _title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (chosen == -1) throw new CancelledByUser();
		return options[chosen];
	}


	public void acknowledgeUnexpectedProblem(String description) {
		acknowledgeUnexpectedProblem(description, null);
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


	public Catcher catcher() {
		return new Catcher() {
			public void catchThis(Throwable throwable) {
				throwable.printStackTrace();
				acknowledgeUnexpectedProblem(throwable.getMessage());
			}
		};
	}


	public boolean confirm(String proposition) throws CancelledByUser {
		int option = JOptionPane.showConfirmDialog(null, proposition);
		if (option == JOptionPane.CANCEL_OPTION) throw new CancelledByUser();
		return option == JOptionPane.YES_OPTION;
	}


	public void acknowledgeUnexpectedProblem(String description, String help) {
		JOptionPane.showMessageDialog(null, description + "\n\n", _title + " - Unexpected Problem", JOptionPane.ERROR_MESSAGE);
		if (help == null) return;
		acknowledgeNotification(help);
		//Fix: Add a "Help" button to the dialog to show the help message.
	}


	public void acknowledge(Throwable t) {
		acknowledgeUnexpectedProblem(t.getLocalizedMessage());
	}
}
