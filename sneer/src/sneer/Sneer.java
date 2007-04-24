package sneer;

import static sneer.SneerDirectories.logDirectory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;

import sneer.kernel.business.Business;
import sneer.kernel.gui.Gui;
import wheel.io.Log;
import wheel.io.ui.User;
import wheel.io.ui.impl.JOptionPaneUser;
import wheel.lang.Threads;

public class Sneer {

	public Sneer() {
		try {
			
			tryToRun();
			 
		} catch (Throwable t) {
			Log.log(t);
			showRestartMessage(t);
		}
	}

	
	private JOptionPaneUser _user = new JOptionPaneUser("Sneer");

	
	private void tryToRun() throws Exception {
		tryToRedirectLogToSneerLogFile();

		startGui();
		
		while (true) Threads.sleepWithoutInterruptions(5000);
	}

	private void startGui() throws Exception {
		Prevayler prevayler = prevaylerFor(new Business());
		new Gui(_user, prevayler);
	}

	private void tryToRedirectLogToSneerLogFile() throws FileNotFoundException {
		logDirectory().mkdir();
		Log.redirectTo(new File(logDirectory(), "log.txt"));
	}

	
	private void showRestartMessage(Throwable t) {
		String description = " " + t.toString() + "\n\n Sneer will now restart.";

		try {
			_user.acknowledgeUnexpectedProblem(description);
		} catch (RuntimeException ignoreHeadlessExceptionForExample) {}
	}

	private Prevayler prevaylerFor(Serializable rootObject) throws Exception {
		return PrevaylerFactory.createPrevayler(rootObject, SneerDirectories.prevalenceDirectory().getAbsolutePath());
	}

	public static void main(String[] args) {
		new Sneer();
	}

}
