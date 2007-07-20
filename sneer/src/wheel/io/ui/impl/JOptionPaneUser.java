package wheel.io.ui.impl;

import java.awt.Component;
import java.awt.Dialog;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;
import wheel.io.ui.Util;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
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
	
	public void confirmWithTimeout(String proposition, int timeout, Omnivore<Boolean> callback) {
		String message = adaptPrompt(proposition);
		JOptionPane pane = new JOptionPane(message,JOptionPane.QUESTION_MESSAGE,JOptionPane.YES_NO_OPTION);
		showOptionPaneWithTimeout(null,pane,timeout,callback);
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
		String string = Util.correctSwingNewlineSpaceProblem(proposition);
		return string + "\n\n";
	}
	
	private void showOptionPaneWithTimeout(JComponent parentComponent, JOptionPane pane, int timeout, Omnivore<Boolean> callback){
		final JDialog dialog = pane.createDialog(parentComponent, _title);
		dialog.setModal(false);
		DialogTimeoutRunner timeoutRunner= new DialogTimeoutRunner(dialog, pane, timeout, callback);
		timeoutRunner.start();
	}
	
	public class DialogTimeoutRunner extends Thread{
		private Dialog _dialog;
		private JOptionPane _pane;
		private Omnivore<Boolean> _callback;
		private int _timeout;
		private String _originalTitle;
		
		public DialogTimeoutRunner(Dialog dialog, JOptionPane pane, int timeout, Omnivore<Boolean> callback){
			_dialog = dialog;
			_pane = pane;
			_callback = callback;
			_timeout = timeout;
			_originalTitle = dialog.getTitle();
		}
		
		@Override
		public void run(){
			_dialog.setVisible(true);
			long start = System.currentTimeMillis();
			while(true){
				Threads.sleepWithoutInterruptions(250); //give cpu a break
				int elapsed = (int)(System.currentTimeMillis() - start ) / 1000;
				if (elapsed>_timeout){
					_callback.consume(false);
					break;
				}	
				_dialog.setTitle(_originalTitle + " (" + (_timeout - elapsed) +" seconds)");
				_dialog.validate();
				Object selectedValue = _pane.getValue();
				if (selectedValue.equals(JOptionPane.UNINITIALIZED_VALUE))
					continue;
				_callback.consume(selectedValue.equals(JOptionPane.YES_OPTION));
				break;
			}
			_dialog.setVisible(false);
			_dialog.dispose();
		}
		
	}
	
}
