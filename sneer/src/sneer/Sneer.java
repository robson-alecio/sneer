package sneer;

import static sneer.kernel.SneerDirectories.logDirectory;

import java.io.File;
import java.io.FileNotFoundException;

import org.prevayler.Prevayler;

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
import wheel.io.ui.impl.TrayIconImpl.SystemTrayNotSupported;
import wheel.lang.Threads;

public class Sneer {

	
	public interface Context {
		User user();
		TrayIcon trayIcon() throws SystemTrayNotSupported;
		Prevayler prevaylerFor(Domain domain) throws Exception;
	}


	public Sneer(Context context) {
		_context = context;
		_user = context.user();
		
		try {
			
			tryToRun();
			 
		} catch (Throwable t) {
			Log.log(t);
			showRestartMessage(t);
		}

	}

	
	private final Context _context;
	
	private final User _user;
	private TrayIcon _trayIcon;
	
	private Prevayler _prevayler;
	private Domain _domain;
	
	
	private void showRestartMessage(Throwable t) {
		String description = " " + t.toString() + "\n\n Sneer will now restart.";

		try {
			_user.acknowledgeUnexpectedProblem(description);
		} catch (RuntimeException ignoreHeadlessExceptionForExample) {}
	}


	private void tryToRun() throws Exception {
		new Installer(_user);
		tryToRedirectLogToSneerLogFile();

		_prevayler = _context.prevaylerFor(new Domain());
		_domain = (Domain)_prevayler.prevalentSystem();
		
		if (_domain.ownName() == null) changeName();
		
		_trayIcon = _context.trayIcon();
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


	public static void main(String[] args) {
		new SneerLive();
	}
	
}
