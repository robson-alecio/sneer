package sneer.kernel.gui;

import java.net.URL;
import java.util.List;
import java.util.Random;

import sneer.kernel.business.BusinessSource;
import sneer.kernel.gui.contacts.ContactAction;
import sneer.kernel.gui.contacts.ShowContactsScreenAction;
import wheel.io.ui.TrayIcon;
import wheel.io.ui.User;
import wheel.io.ui.TrayIcon.Action;
import wheel.io.ui.impl.TrayIconImpl;
import wheel.io.ui.impl.ValueChangePane;
import wheel.lang.IntegerParser;
import wheel.lang.exceptions.IllegalParameter;

public class Gui {

	public Gui(User user, BusinessSource businessSource, List<ContactAction> contactActions) throws Exception {
		_user = user;
		_businessSource = businessSource;
		_contactActions = contactActions;

		URL icon = Gui.class.getResource("/sneer/kernel/gui/traymenu/yourIconGoesHere.png");
		_trayIcon = new TrayIconImpl(icon, _user.catcher());
	
		tryToRun();
	}


	private final User _user;
	private final TrayIcon _trayIcon;
	
	private final BusinessSource _businessSource;
	private final List<ContactAction> _contactActions;
	
	
	private void tryToRun() {
		filloutInitialValues();

		_trayIcon.addAction(nameChangeAction());
		_trayIcon.addAction(new ShowContactsScreenAction(_businessSource.output().contacts(), _businessSource.contactAdder(), _user, _contactActions));
		_trayIcon.addAction(sneerPortChangeAction());
		_trayIcon.addAction(exitAction());
	}

	private void filloutInitialValues() { //Refactor: remove this logic from the gui;
		String ownName = _businessSource.output().ownName().currentValue();
		if (ownName == null || ownName.isEmpty()) nameChangeAction().run();

		int sneerPort = _businessSource.output().sneerPort().currentValue();
		if (sneerPort == 0) initSneerPort();
	}

	private void initSneerPort() {
		int randomPort = 10000 + new Random().nextInt(50000);
		try {
			_businessSource.sneerPortSetter().consume(randomPort);
		} catch (IllegalParameter e) {
			throw new IllegalStateException();
		}
		sneerPortChangeAction().run();
	}


	private ValueChangePane sneerPortChangeAction() {
		String prompt = " Change this only if you know what you are doing." +
						"\n Sneer TCP port to listen:";
		return new ValueChangePane("Sneer Port Configuration",prompt, _user, _businessSource.output().sneerPort(), new IntegerParser(_businessSource.sneerPortSetter()));
	}

	
	private Action nameChangeAction() {
		String prompt = " What is your name?" +
						"\n (You can change it any time you like)";
		return new ValueChangePane("Own Name",prompt, _user, _businessSource.output().ownName(), _businessSource.ownNameSetter());
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
