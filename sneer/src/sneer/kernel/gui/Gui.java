package sneer.kernel.gui;

import java.net.URL;
import java.util.Random;

import sneer.kernel.business.BusinessSource;
import sneer.kernel.gui.contacts.ShowContactsScreenAction;
import wheel.io.ui.TrayIcon;
import wheel.io.ui.User;
import wheel.io.ui.TrayIcon.Action;
import wheel.io.ui.impl.TrayIconImpl;
import wheel.io.ui.impl.ValueChangePane;
import wheel.lang.IntegerParser;
import wheel.lang.exceptions.IllegalParameter;

public class Gui {

	public static void start(User user, BusinessSource persistentBusinessSource) throws Exception {
		new Gui(user, persistentBusinessSource);
	}
	
	private Gui(User user, BusinessSource businessSource) throws Exception {
		_user = user;
		_business = businessSource;

		URL icon = Gui.class.getResource("/sneer/kernel/gui/traymenu/yourIconGoesHere.png");
		_trayIcon = new TrayIconImpl(icon, _user.catcher());
	
		tryToRun();
	}


	private final User _user;
	private final TrayIcon _trayIcon;
	
	private final BusinessSource _business;
	
	
	private void tryToRun() {
		filloutInitialValues();

		_trayIcon.addAction(nameChangeAction());
		_trayIcon.addAction(new ShowContactsScreenAction(_business.output().contacts(), _business.contactAdder(), _user));
		_trayIcon.addAction(sneerPortChangeAction());
		_trayIcon.addAction(exitAction());
	}

	private void filloutInitialValues() { //Refactor: remove this logic from the gui;
		String ownName = _business.output().ownName().currentValue();
		if (ownName == null || ownName.isEmpty()) nameChangeAction().run();

		int sneerPort = _business.output().sneerPort().currentValue();
		if (sneerPort == 0) initSneerPort();
	}

	private void initSneerPort() {
		int randomPort = 10000 + new Random().nextInt(50000);
		try {
			_business.sneerPortSetter().consume(randomPort);
		} catch (IllegalParameter e) {
			throw new IllegalStateException();
		}
		sneerPortChangeAction().run();
	}


	private ValueChangePane sneerPortChangeAction() {
		String prompt = " Change this only if you know what you are doing." +
						"\n Sneer TCP port to listen:";
		return new ValueChangePane("Sneer Port Configuration",prompt, _user, _business.output().sneerPort(), new IntegerParser(_business.sneerPortSetter()));
	}

	
	private Action nameChangeAction() {
		String prompt = " What is your name?" +
						"\n (You can change it any time you like)";
		return new ValueChangePane("Name Change",prompt, _user, _business.output().ownName(), _business.ownNameSetter());
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
