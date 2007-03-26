package sneer;

import static sneer.kernel.SneerDirectories.logDirectory;

import java.io.File;
import java.io.FileNotFoundException;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;

import sneer.kernel.ContactsListing;
import sneer.kernel.Domain;
import sneer.kernel.NameChange;
import sneer.kernel.NewContactAddition;
import sneer.kernel.SneerDirectories;
import sneer.kernel.install.Installer;
import wheel.io.Log;
import wheel.io.ui.SwingUser;
import wheel.io.ui.SwingUser.Action;
import wheel.lang.Threads;

public class Sneer {


	private SwingUser _swingUser;
	private Prevayler _prevayler;
	private Domain _domain;


	public Sneer() {
		try {
			tryToRun();
		} catch (Throwable t) {
			Log.log(t);
		}
	}

	private void tryToRun() throws Exception {
		_swingUser = new SwingUser(Sneer.class.getResource("/sneer/gui/traymenu/yourIconGoesHere.png"));
		
		new Installer(_swingUser);
		tryToRedirectLogToSneerLogFile();

		_prevayler = PrevaylerFactory.createPrevayler(new Domain(), SneerDirectories.prevalenceDirectory().getAbsolutePath());
		_domain = (Domain)_prevayler.prevalentSystem();
		
		if (_domain.ownName() == null)
			changeName();
		
		_swingUser.addAction(nameChangeAction());
		_swingUser.addAction(listContactsAction());
		_swingUser.addAction(addNewContactAction());
		_swingUser.addAction(exitAction());

		while (true) Threads.sleepWithoutInterruptions(5000);
	}

	private void changeName() {
		_prevayler.execute(new NameChange(_swingUser, _domain));
	}

	
	private Action addNewContactAction() {
		return new Action(){

			public String caption() {
				return "Add New Contact";
			}

			public void run() {
				_prevayler.execute(new NewContactAddition(_swingUser));
			}
		};
	}

	
	private Action listContactsAction() {
		return new Action(){
			public String caption() {
				return "List Contacts";
			}

			public void run() {
				new ContactsListing(_swingUser, _domain);
			}
		};
	}

	private Action nameChangeAction() {
		return new Action(){

			public String caption() {
				return "Change your Name";
			}

			public void run() {
				changeName();
			}
		};
	}

	private void tryToRedirectLogToSneerLogFile() throws FileNotFoundException {
		logDirectory().mkdir();
		Log.redirectTo(new File(logDirectory(), "log.txt"));
	}

	
	private Action exitAction() {
		return new Action(){

			public String caption() {
				return "Exit";
			}

			public void run() {
				System.exit(0);
			}
		};
	}
}
