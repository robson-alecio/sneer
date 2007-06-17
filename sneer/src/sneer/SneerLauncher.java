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
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.impl.Communicator;
import sneer.kernel.gui.Gui;
import sneer.kernel.gui.contacts.ContactAction;
import wheel.io.Log;
import wheel.io.network.OldNetworkImpl;
import wheel.io.network.impl.XStreamNetwork;
import wheel.io.ui.User;
import wheel.io.ui.impl.JOptionPaneUser;
import wheel.lang.Threads;

public class SneerLauncher {

	private final User _user = new JOptionPaneUser("Sneer");
	
	public SneerLauncher() {
		try {
			
			new Sneer(_user);
			 
		} catch (Throwable throwable) {
			Log.log(throwable);
			showRestartMessage(throwable);
		}
	}
	

	private void showRestartMessage(Throwable t) {
		String description = " " + t.getLocalizedMessage() + "\n\n Sneer will now restart.";

		try {
			_user.acknowledgeUnexpectedProblem(description);
		} catch (RuntimeException ignoreHeadlessExceptionForExample) {}
	}
	
	private static class Sneer{
		private final Communicator _communicator;
		private final User _user;

		
		public Sneer(User user) throws Exception {
			_user = user;
			tryToRedirectLogToSneerLogFile();
			
			Prevayler prevayler = prevaylerFor(new BusinessFactory().createBusinessSource());
			BusinessSource persistentBusinessSource = Bubble.wrapStateMachine(prevayler);
			
			_communicator = new Communicator(_user, new XStreamNetwork(new OldNetworkImpl()), persistentBusinessSource);
			new Gui(_user, persistentBusinessSource, contactActions()); //Implement: start the gui before having the BusinessSource ready. Use a callback to get the BusinessSource.
			
			while (true) Threads.sleepWithoutInterruptions(5000);
		}

		private List<ContactAction> contactActions() {
			List<ContactAction> result = new ArrayList<ContactAction>();
			Channel conversationsAppChannel = _communicator.getChannel(ConversationsApp.class.getName());
			result.add(new ConversationsApp(conversationsAppChannel).contactAction());
			return result;
		}

		private void tryToRedirectLogToSneerLogFile() throws FileNotFoundException {
			logDirectory().mkdir();
			Log.redirectTo(new File(logDirectory(), "log.txt"));
		}

		private Prevayler prevaylerFor(Object rootObject) throws Exception {
			PrevaylerFactory factory = new PrevaylerFactory();
			factory.configureTransactionFiltering(false);
			factory.configurePrevalentSystem(rootObject);
			factory.configurePrevalenceDirectory(SneerDirectories.prevalenceDirectory().getAbsolutePath());
			factory.configureJournalSerializer(new XStreamSerializer());
			factory.configureSnapshotSerializer(new XStreamSerializer());
			return factory.create();
		}

		public static void main(String[] args) {
			new SneerLauncher();
		}
	
	}	
	
}
