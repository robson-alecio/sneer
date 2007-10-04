package sneer;

import static sneer.SneerDirectories.logDirectory;

import java.io.File;
import java.io.FileNotFoundException;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;

import prevayler.bubble.Bubble;
import sneer.apps.sharedfolder.SharedFolder;
import sneer.apps.transferqueue.TransferQueue;
import sneer.kernel.appmanager.AppManager;
import sneer.kernel.appmanager.SovereignApplicationUID;
import sneer.kernel.business.BusinessSource;
import sneer.kernel.business.impl.BusinessFactory;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.impl.Communicator;
import sneer.kernel.gui.Gui;
import sneer.kernel.gui.contacts.ContactActionFactory;
import sneer.kernel.pointofview.Party;
import sneer.kernel.pointofview.impl.Me;
import wheel.i18n.Language;
import wheel.io.Log;
import wheel.io.network.OldNetworkImpl;
import wheel.io.ui.User;
import wheel.io.ui.User.Notification;
import wheel.io.ui.impl.JOptionPaneUser;
import wheel.lang.Omnivore;
import wheel.lang.Threads;

public class Sneer {

	public static void main(String args[]) throws Exception {
		new Sneer();
	}
	
	public Sneer() throws Exception{
		try {
			
			tryToRun();
			 
		} catch (Throwable throwable) {
			Log.log(throwable);
			showExitMessage(throwable);
			System.exit(-1);
		}
	}
	
	private User _user;
	private BusinessSource _businessSource;
	private Communicator _communicator;
	private Party _me;
	private Gui _gui;
	private TransferQueue _transfer;
	private AppManager _appManager;
	private ContactActionFactory _contactActionFactory;
	private SharedFolder _sharedFolder;
	
	private void tryToRun() throws Exception {
		
		tryToRedirectLogToSneerLogFile();

		Prevayler prevayler = prevaylerFor(new BusinessFactory().createBusinessSource());
		_businessSource = Bubble.wrapStateMachine(prevayler);

		_user = new JOptionPaneUser("Sneer", briefNotifier());
		
		initLanguage();
		
		//Optimize: Separate thread to close splash screen.
		try{Thread.sleep(2000);}catch(InterruptedException ie){}
		
		_communicator = new Communicator(_user, new OldNetworkImpl(), _businessSource);
		
		Channel channel = _communicator.openChannel("Point of View", 1);
		_me = new Me(_businessSource.output(), _communicator.operator(), channel);
		
		Channel transferChannel = _communicator.openChannel("Transfer", 2);
		_transfer = new TransferQueue(transferChannel);
		
		Channel sharedFolderChannel = _communicator.openChannel("Shared Folder", 2);
		_sharedFolder = new SharedFolder(sharedFolderChannel, _me.contacts(), _transfer);
		
		System.out.println("Checking existing apps:");
		_appManager = new AppManager(_user,_communicator, _me, _businessSource.output().contactAttributes(), briefNotifier(),_transfer);
		for(SovereignApplicationUID app:_appManager.publishedApps().output())
			System.out.println("App : "+app._sovereignApplication.defaultName());
		
		_contactActionFactory = new ContactActionFactory(_user, _communicator, _appManager, _transfer, _sharedFolder);
		
		_gui = new Gui(_user, _me, _businessSource, _appManager, _contactActionFactory); //Implement:  start the gui before having the BusinessSource ready. Use a callback to get the BusinessSource.
		
		while (true) Threads.sleepWithoutInterruptions(100000); // Refactor Consider joining the main gui thread.
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
