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
import wheel.io.ui.TrayIcon;
import wheel.io.ui.TrayIcon.Action;
import wheel.io.ui.User;
import wheel.io.ui.impl.JOptionPaneUser;
import wheel.io.ui.impl.TrayIconImpl;
import wheel.lang.Threads;

public class Sneer {


	private User _user;
	private TrayIcon _trayIcon;

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
		_user = new JOptionPaneUser();
		_trayIcon = new TrayIconImpl(Sneer.class.getResource("/sneer/gui/traymenu/yourIconGoesHere.png"));
		
		new Installer(_user);
		tryToRedirectLogToSneerLogFile();

		_prevayler = PrevaylerFactory.createPrevayler(new Domain(), SneerDirectories.prevalenceDirectory().getAbsolutePath());
		_domain = (Domain)_prevayler.prevalentSystem();
		
		if (_domain.ownName() == null)
			changeName();
		
		_trayIcon.addAction(nameChangeAction());
		_trayIcon.addAction(listContactsAction());
		_trayIcon.addAction(addNewContactAction());
		_trayIcon.addAction(exitAction());

		while (true) Threads.sleepWithoutInterruptions(5000);
	}

	private void changeName() {
		_prevayler.execute(new NameChange(_user, _domain));
	}

	
	private Action addNewContactAction() {
		return new Action(){

			public String caption() {
				return "Add New Contact";
			}

			public void run() {
				_prevayler.execute(new NewContactAddition(_user));
			}
		};
	}

	
	private Action listContactsAction() {
		return new Action(){
			public String caption() {
				return "List Contacts";
			}

			public void run() {
				new ContactsListing(_user, _domain);
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
