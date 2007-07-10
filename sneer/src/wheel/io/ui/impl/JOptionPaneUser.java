package wheel.io.ui.impl;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;
import wheel.lang.exceptions.Catcher;
import wheel.lang.exceptions.FriendlyException;

public class JOptionPaneUser implements User {
	
	public JOptionPaneUser(String title) { 
		//Fix: receive the parent component instead of passing null to the JOptionPane in order not to be application modal.
		_title = title;
	}

	
	private final String _title;

	
	@Override
	public Object choose(String proposition, Object... options) throws CancelledByUser {
		int chosen = showOptionDialog(null, proposition, _title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (chosen == -1) throw new CancelledByUser();
		return options[chosen];
	}


	@Override
	public void acknowledgeUnexpectedProblem(String description) {
		acknowledgeUnexpectedProblem(description, null);
	}

	
	@Override
	public String answer(String prompt) throws CancelledByUser {
		return answer(prompt, "");
	}

	
	@Override
	public String answer(String prompt, String defaultAnswer) throws CancelledByUser {
		String answer = showInputDialog(null, prompt, defaultAnswer);
		if (answer == null) throw new CancelledByUser();
		return answer;
	}

	
	@Override
	public void acknowledgeNotification(String notification) {
		acknowledgeNotification(notification, "OK");
	}


	@Override
	public void acknowledgeNotification(String notification, String acknowledgement) {
		try {
			choose(notification, acknowledgement);
		} catch (CancelledByUser e) {}
	}


	@Override
	public Catcher catcher() {
		return new Catcher() {
			public void catchThis(Throwable throwable) {
				throwable.printStackTrace();
				acknowledgeUnexpectedProblem(throwable.getMessage());
			}
		};
	}


	public boolean confirm(String proposition) {
		try {
			return confirmOrCancel(proposition);
		} catch (CancelledByUser e) {
			return false;
		}
	}

	@Override
	public boolean confirmOrCancel(String proposition) throws CancelledByUser {
		int option = showConfirmDialog(null, proposition);
		if (option == JOptionPane.CANCEL_OPTION) throw new CancelledByUser();
		return option == JOptionPane.YES_OPTION;
	}


	@Override
	public void acknowledgeUnexpectedProblem(String description, String help) {
		showMessageDialog(null, description, _title + " - Unexpected Problem", JOptionPane.ERROR_MESSAGE);
		if (help == null) return;
		acknowledgeNotification(help);
		//Fix: Add a "Help" button to the dialog to show the help message.
	}


	@Override
	public void acknowledge(Throwable t) {
		acknowledgeUnexpectedProblem(t.getLocalizedMessage());
	}


	@Override
	public void acknowledgeFriendlyException(FriendlyException e) {
		acknowledgeUnexpectedProblem(e.getMessage(), e.getHelp());
		
	}
	
	private int showOptionDialog(Component parentComponent, String message, String title, int optionType, int messageType, Icon icon, Object[] options, Object initialValue){
		String proposition = adaptPrompt(message);
		return JOptionPane.showOptionDialog(parentComponent, proposition, title, optionType, messageType, icon, options, initialValue);
	}
	
	private void showMessageDialog(Component parentComponent, String message, String title, int messageType){
		String proposition = adaptPrompt(message);
		JOptionPane.showMessageDialog(parentComponent, proposition, title, messageType);
	}

	private String showInputDialog(Component parentComponent, String message, Object initialValue){
		String proposition = adaptPrompt(message);
		return JOptionPane.showInputDialog(parentComponent, proposition, initialValue);
	}
	
	private int showConfirmDialog(Component parentComponent, String message){
		String proposition = adaptPrompt(message);
		return JOptionPane.showConfirmDialog(parentComponent, proposition);
	}

	private String adaptPrompt(String proposition) {
		String string = correctSwingNewlineSpaceProblem(proposition);
		return string + "\n\n";
	}


	private String correctSwingNewlineSpaceProblem(String proposition) {
		return " " + proposition.replaceAll("\\n", "\n ");
	}
	

}
