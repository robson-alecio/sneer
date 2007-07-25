package sneer;

import static sneer.SneerDirectories.logDirectory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;

import prevayler.bubble.Bubble;
import sneer.apps.conversations.ConversationsApp;
import sneer.apps.filetransfer.FileTransferApp;
import sneer.apps.scribble.ScribbleApp;
import sneer.apps.talk.TalkApp;
import sneer.kernel.business.BusinessSource;
import sneer.kernel.business.impl.BusinessFactory;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.impl.Communicator;
import sneer.kernel.gui.Gui;
import sneer.kernel.gui.contacts.ContactAction;
import wheel.i18n.Language;
import wheel.io.Log;
import wheel.io.files.Directory;
import wheel.io.files.impl.DurableDirectory;
import wheel.io.network.OldNetworkImpl;
import wheel.io.network.impl.XStreamNetwork;
import wheel.io.ui.JFrameBoundsKeeper;
import wheel.io.ui.User;
import wheel.io.ui.User.Notification;
import wheel.io.ui.impl.BoundsPersistence;
import wheel.io.ui.impl.DirectoryBoundsPersistence;
import wheel.io.ui.impl.JFrameBoundsKeeperImpl;
import wheel.io.ui.impl.JOptionPaneUser;
import wheel.lang.Omnivore;
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

	
	private User _user = new JOptionPaneUser("Sneer", briefNotifier());
	private Communicator _communicator;
	private BusinessSource _businessSource;
	private Gui _gui;
	private JFrameBoundsKeeperImpl _jframeBoundsKeeper;

	
	private void tryToRun() throws Exception {
		tryToRedirectLogToSneerLogFile();

		Prevayler prevayler = prevaylerFor(new BusinessFactory().createBusinessSource());
		_businessSource = Bubble.wrapStateMachine(prevayler);

		initLanguage();
		
		//Fix: small delay to show splash. in the future will be used to plugin/resource download
		try{Thread.sleep(2000);}catch(InterruptedException ie){} 
		
		_communicator = new Communicator(_user, new XStreamNetwork(new OldNetworkImpl()), _businessSource);
		_gui = new Gui(_user, _businessSource, contactActions(), jFrameBoundsKeeper()); //Implement:  start the gui before having the BusinessSource ready. Use a callback to get the BusinessSource.
		
		while (true) Threads.sleepWithoutInterruptions(100000); // Refactor Consider joining the main gui thread.
	}

	private JFrameBoundsKeeper jFrameBoundsKeeper() throws IOException {
		if (_jframeBoundsKeeper != null) return _jframeBoundsKeeper;
		
		Directory directory = new DurableDirectory(SneerDirectories.sneerDirectory().getPath());
		BoundsPersistence boundsPersistence = new DirectoryBoundsPersistence(directory);
		return _jframeBoundsKeeper = new JFrameBoundsKeeperImpl(boundsPersistence);
	}

	private Omnivore<Notification> briefNotifier() {
		return new Omnivore<Notification>() { @Override public void consume(Notification notification) {
			_gui.briefNotifier().consume(notification);
		}};
	}

	private void initLanguage() {
		String current = System.getProperty("sneer.language");
		if (current == null || current.isEmpty()) current = "en";
		
		String chosen = _businessSource.output().language().currentValue();
		if (chosen == null || chosen.isEmpty()) {
			_businessSource.languageSetter().consume(current);
			chosen = current;
		} 
		
		if (chosen.equals("en"))
			Language.reset();
		else
			Language.load(chosen);
	}

	private List<ContactAction> contactActions() {
		List<ContactAction> result = new ArrayList<ContactAction>();
		
		Channel conversationsChannel = _communicator.getChannel(ConversationsApp.class.getName(), 0);
		result.add(new ConversationsApp(conversationsChannel, _businessSource.output().contactAttributes(), _user.briefNotifier(), _jframeBoundsKeeper).contactAction());
		
		Channel talkChannel = _communicator.getChannel(TalkApp.class.getName(), 1);
		result.add(new TalkApp(_user, talkChannel, _businessSource.output().contactAttributes()).contactAction());
		
		Channel fileTransferChannel = _communicator.getChannel(FileTransferApp.class.getName(), 2);
		result.add(new FileTransferApp(_user, fileTransferChannel, _businessSource.output().contactAttributes()).contactAction());
		
		Channel scribbleChannel = _communicator.getChannel(ScribbleApp.class.getName(), 2);
		result.add(new ScribbleApp(_user, scribbleChannel, _businessSource.output().contactAttributes()).contactAction());
		
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
		return factory.create();
	}

}
