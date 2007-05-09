package sneer;

import static sneer.SneerDirectories.logDirectory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;

import sneer.kernel.business.BusinessFactory;
import sneer.kernel.communication.Communicator;
import sneer.kernel.gui.Gui;
import wheel.io.Log;
import wheel.io.network.OldNetworkImpl;
import wheel.io.ui.User;
import wheel.io.ui.impl.JOptionPaneUser;
import wheel.lang.Threads;

public class Sneer {

	public Sneer() {
		try {
			
			tryToRun();
			 
		} catch (Throwable throwable) {
			Log.log(throwable);
			showRestartMessage(throwable);
		}
	}

	
	private User _user = new JOptionPaneUser("Sneer");

	
	private void tryToRun() throws Exception {
		tryToRedirectLogToSneerLogFile();

		Prevayler prevayler = prevaylerFor((Serializable)new BusinessFactory().createBusinessSource());
		new Communicator(_user, new OldNetworkImpl(), prevayler);
		new Gui(_user, prevayler);
		
		
		while (true) Threads.sleepWithoutInterruptions(5000);
	}

	private void tryToRedirectLogToSneerLogFile() throws FileNotFoundException {
		logDirectory().mkdir();
		Log.redirectTo(new File(logDirectory(), "log.txt"));
	}

	
	private void showRestartMessage(Throwable t) {
		String description = " " + t.getLocalizedMessage() + "\n\n Sneer will now restart.";

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
