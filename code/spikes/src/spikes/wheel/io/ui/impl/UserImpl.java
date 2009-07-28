package spikes.wheel.io.ui.impl;

import static sneer.foundation.environments.Environments.my;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dialog.ModalityType;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.gui.guithread.GuiThread;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.exceptions.FriendlyException;
import spikes.wheel.io.ui.CancelledByUser;
import spikes.wheel.io.ui.User;
import spikes.wheel.io.ui.Util;
import spikes.wheel.lang.exceptions.Catcher;

public class UserImpl implements User {


	private String _title;
	private Consumer<Notification> _briefNotifier;

	public UserImpl() {
		//used by SimpleContainer
	} 

	public UserImpl(String title, Consumer<Notification> briefNotifier) { 
		//Fix: receive the parent component instead of passing null to the JOptionPane in order not to be application modal.
		_title = title;
		_briefNotifier = briefNotifier;
	}

	

	
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
	
	public void confirmWithTimeout(String proposition, int timeout, Consumer<Boolean> callback) {
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
		String string = Util.workAroundSwingNewlineSpaceProblem(proposition);
		return string + "\n\n";
	}
	
	private void showModelessOptionPane(String title, String message) {
		JOptionPane pane = new JOptionPane(message);
		Dialog dialog = pane.createDialog(title);
		dialog.setModalityType(ModalityType.MODELESS);
		dialog.setVisible(true);
	}
	
	private void showOptionPaneWithTimeout(JComponent parentComponent, JOptionPane pane, int timeout, Consumer<Boolean> callback){
		final JDialog dialog = pane.createDialog(parentComponent, _title);
		dialog.setModal(false);
		DialogTimeoutRunner timeoutRunner= new DialogTimeoutRunner(dialog, pane, timeout, callback);
		timeoutRunner.start();
	}
	
	public class DialogTimeoutRunner extends Thread{
		private Dialog _dialog;
		private JOptionPane _pane;
		private Consumer<Boolean> _callback;
		private int _timeout;
		private String _originalTitle;
		
		public DialogTimeoutRunner(Dialog dialog, JOptionPane pane, int timeout, Consumer<Boolean> callback){
			_dialog = dialog;
			_pane = pane;
			_callback = callback;
			_timeout = timeout;
			_originalTitle = dialog.getTitle();
		}
		
		@Override
		public void run(){
			_dialog.setTitle(_originalTitle + " (Timeout " + _timeout +" seconds)");
			_dialog.setVisible(true);
			long start = System.currentTimeMillis();
			while(true){
				my(Threads.class).sleepWithoutInterruptions(250); //give cpu a break
				int elapsed = (int)(System.currentTimeMillis() - start ) / 1000;
				if (elapsed>_timeout){
					_callback.consume(false);
					break;
				}	
				Object selectedValue = _pane.getValue();
				if (selectedValue==null)
					break;
				if (selectedValue.equals(JOptionPane.UNINITIALIZED_VALUE))
					continue;
				_callback.consume(selectedValue.equals(JOptionPane.YES_OPTION));
				break;
			}
			_dialog.setVisible(false);
			_dialog.dispose();
		}
		
	}

	@Override
	public Consumer<Notification> briefNotifier() {
		return _briefNotifier;
	}

	@Override
	public void saveAs(final String title, final String buttonTitle, final String[] suffixes, final String description, final Consumer<File> callback) {
		my(GuiThread.class).invokeLater(new Runnable(){ public void run(){
			final JFileChooser fc = new JFileChooser(); 
			fc.setDialogTitle(title);
			fc.setApproveButtonText(buttonTitle);
			fc.setFileFilter(new FileFilter(){ @Override
				public boolean accept(File f) {
				if (suffixes==null) return true;
				for(String suffix:suffixes)
					if ((f.isDirectory())||(f.getName().toLowerCase().endsWith(suffix))) 
						return true;
				return false;
				}
				@Override
				public String getDescription() {
					return description;
				}});
			int value = fc.showSaveDialog(null);
			if (value != JFileChooser.APPROVE_OPTION) return;
			File file = fc.getSelectedFile();
			callback.consume(file);
		}});
	}
	
	@Override
	public void chooseFolder(final String title, final String buttonTitle, final Consumer<File> callback) {
		my(GuiThread.class).invokeLater(new Runnable(){ public void run(){
			final JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.setApproveButtonText(buttonTitle);
			fc.setDialogTitle(title);
		
			if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
				File result = fc.getSelectedFile();
				if (!result.isDirectory())  // User might have entered manually.
					acknowledgeNotification("This is not a valid folder:\n\n%1$s\n\nTry again.", result.getPath());
				else
					callback.consume(result);
			}else{
				callback.consume(null); //callback must check for null and consider it as user cancelation
			}
		}});
	}

	public void modelessAcknowledge(String title, String message) {
		showModelessOptionPane(title, message);
	}
	
}
