package sneer;

import static sneer.SneerDirectories.logDirectory;

import java.io.File;
import java.io.FileNotFoundException;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.prevayler.foundation.serialization.XStreamSerializer;

import prevayler.bubble.Bubble;
import sneer.kernel.business.BusinessSource;
import sneer.kernel.business.impl.BusinessFactory;
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

		Prevayler prevayler = prevaylerFor(new BusinessFactory().createBusinessSource());
		BusinessSource persistentBusinessSource = Bubble.wrapStateMachine(prevayler);

		new Gui(_user, persistentBusinessSource);
		new Communicator(_user, new OldNetworkImpl(), persistentBusinessSource.output());
		
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

	private Prevayler prevaylerFor(Object rootObject) throws Exception {
		PrevaylerFactory factory = new PrevaylerFactory();
		factory.configurePrevalentSystem(rootObject);
		factory.configurePrevalenceDirectory(SneerDirectories.prevalenceDirectory().getAbsolutePath());
		factory.configureJournalSerializer(new XStreamSerializer());
		factory.configureSnapshotSerializer(new XStreamSerializer());
		return factory.create();
	}

	public static void main(String[] args) {
		new Sneer();
	}

}
