package sneer.kernel.gui;

import static sneer.SneerDirectories.logDirectory;

import java.io.File;
import java.io.FileNotFoundException;

import org.prevayler.Prevayler;

import sneer.Sneer;
import sneer.kernel.business.Business;
import wheel.io.Log;
import wheel.io.ui.CancelledByUser;
import wheel.io.ui.TrayIcon;
import wheel.io.ui.User;
import wheel.io.ui.TrayIcon.Action;
import wheel.io.ui.impl.TrayIconImpl;
import wheel.io.ui.impl.TrayIconImpl.SystemTrayNotSupported;
import wheel.lang.Threads;

public class Gui {

	public Gui(User user, Prevayler prevayler) throws Exception {
		_user = user;

		_prevayler = prevayler;
		_business = (Business)_prevayler.prevalentSystem();

		_trayIcon = new TrayIconImpl(Gui.class.getResource("/sneer/kernel/gui/traymenu/yourIconGoesHere.png"));
		
		tryToRun();
	}


	private final User _user;
	private final TrayIcon _trayIcon;
	
	private final Prevayler _prevayler;
	private final Business _business;
	
	
	private void tryToRun() throws Exception {
		//Refactor remove this logic from the gui;
		if (_business.ownName() == null) changeName();
		if (_business.sneerPortNumber() == 0) changeSneerPort();

		_trayIcon.addAction(nameChangeAction());
		_trayIcon.addAction(addNewContactAction());
		_trayIcon.addAction(listContactsAction());
		_trayIcon.addAction(exitAction());
	}

	private void changeName() {
		try {
			_prevayler.execute(new NameChange(_user, _business.ownName()));
		} catch (CancelledByUser e) {}
	}

	private void changeSneerPort() {
		//Implement
	}

	
	private Action addNewContactAction() {
		return new Action(){

			public String caption() {
				return "Add New Contact";
			}

			public void run() {
				try {
					_prevayler.execute(new NewContactAddition(_user));
				} catch (CancelledByUser e) {}
			}
		};
	}

	
	private Action listContactsAction() {
		return new Action(){
			public String caption() {
				return "List Contacts";
			}

			public void run() {
				new ContactsListing(_user, _business);
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
