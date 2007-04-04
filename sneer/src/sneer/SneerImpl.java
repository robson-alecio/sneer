package sneer;

import static sneer.SneerDirectories.logDirectory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;

import org.prevayler.Prevayler;

import sneer.kernel.business.essence.Essence;
import sneer.kernel.gui.ContactsListing;
import sneer.kernel.gui.NameChange;
import sneer.kernel.gui.NewContactAddition;
import wheel.io.Log;
import wheel.io.ui.CancelledByUser;
import wheel.io.ui.TrayIcon;
import wheel.io.ui.User;
import wheel.io.ui.TrayIcon.Action;
import wheel.io.ui.impl.TrayIconImpl.SystemTrayNotSupported;
import wheel.lang.Threads;

public class SneerImpl {

	
	public interface Context {
		User user();
		TrayIcon trayIcon() throws SystemTrayNotSupported;
		Prevayler prevaylerFor(Serializable rootObject) throws Exception;
	}


	public SneerImpl(Context context) {
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
	private Essence _essence;
	
	
	private void tryToRun() throws Exception {
		tryToRedirectLogToSneerLogFile();

		_prevayler = _context.prevaylerFor(new Essence());
		_essence = (Essence)_prevayler.prevalentSystem();
		
		if (_essence.ownName() == null) changeName();
		
		_trayIcon = _context.trayIcon();
		_trayIcon.addAction(nameChangeAction());
		_trayIcon.addAction(listContactsAction());
		_trayIcon.addAction(addNewContactAction());
		_trayIcon.addAction(exitAction());

		while (true) Threads.sleepWithoutInterruptions(5000);
	}

	private void changeName() {
		try {
			_prevayler.execute(new NameChange(_user, _essence.ownName()));
		} catch (CancelledByUser e) {}
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
				new ContactsListing(_user, _essence);
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

	
	private void showRestartMessage(Throwable t) {
		String description = " " + t.toString() + "\n\n Sneer will now restart.";

		try {
			_user.acknowledgeUnexpectedProblem(description);
		} catch (RuntimeException ignoreHeadlessExceptionForExample) {}
	}

	
}
