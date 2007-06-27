package sneer;

import static sneer.SneerDirectories.logDirectory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.prevayler.foundation.serialization.XStreamSerializer;

import prevayler.bubble.Bubble;
import sneer.apps.conversations.ConversationsApp;
import sneer.kernel.business.BusinessSource;
import sneer.kernel.business.impl.BusinessFactory;
import sneer.kernel.communication.impl.Communicator;
import sneer.kernel.gui.Gui;
import sneer.kernel.gui.contacts.ContactAction;
import wheel.io.Log;
import wheel.io.network.OldNetworkImpl;
import wheel.io.network.impl.XStreamNetwork;
import wheel.io.ui.User;
import wheel.io.ui.impl.JOptionPaneUser;
import wheel.lang.Threads;

public class Sneer {

	public Sneer() {
		try {
			
			tryToRun();
			 
		} catch (Throwable throwable) {
			Log.log(throwable);
			showExitMessage(throwable);
			System.exit(-1);
		}
	}

	
	private User _user = new JOptionPaneUser("Sneer");
	private Communicator _communicator;

	
	private void tryToRun() throws Exception {
		tryToRedirectLogToSneerLogFile();

		Prevayler prevayler = prevaylerFor(new BusinessFactory().createBusinessSource());
		BusinessSource persistentBusinessSource = Bubble.wrapStateMachine(prevayler);

		_communicator = new Communicator(_user, new XStreamNetwork(new OldNetworkImpl()), persistentBusinessSource);
		new Gui(_user, persistentBusinessSource, contactActions()); //Implement:  start the gui before having the BusinessSource ready. Use a callback to get the BusinessSource.
		
		while (true) Threads.sleepWithoutInterruptions(5000);
	}

	private List<ContactAction> contactActions() {
		List<ContactAction> result = new ArrayList<ContactAction>();
		result.add(new ConversationsApp(_communicator.getChannel(ConversationsApp.class.getName())).contactAction());
		return result;
	}

	private void tryToRedirectLogToSneerLogFile() throws FileNotFoundException {
		logDirectory().mkdir();
		Log.redirectTo(new File(logDirectory(), "log.txt"));
	}

	
	private void showExitMessage(Throwable t) {
		String description = " " + t.getLocalizedMessage() + "\n\n Sneer will now exit.";

		try {
			_user.acknowledgeUnexpectedProblem(description);
		} catch (RuntimeException ignoreHeadlessExceptionForExample) {}
	}

	private Prevayler prevaylerFor(Object rootObject) throws Exception {
		PrevaylerFactory factory = new PrevaylerFactory();
		factory.configureTransactionFiltering(false);
		factory.configurePrevalentSystem(rootObject);
		factory.configurePrevalenceDirectory(SneerDirectories.prevalenceDirectory().getAbsolutePath());
		factory.configureSnapshotSerializer(new XStreamSerializer());
		factory.configureJournalSerializer(new XStreamSerializer());
		return factory.create();
	}

}
