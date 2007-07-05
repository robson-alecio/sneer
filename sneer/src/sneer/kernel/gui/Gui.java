package sneer.kernel.gui;

import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static wheel.i18n.Language.*;
import sneer.kernel.business.BusinessSource;
import sneer.kernel.gui.contacts.ContactAction;
import sneer.kernel.gui.contacts.ShowContactsScreenAction;
import wheel.i18n.Language;
import wheel.io.ui.CancelledByUser;
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
		bindActionsToTrayIcon();
		
		showContactsScreenAction().run();
	}

	void bindActionsToTrayIcon() {
		_trayIcon.clearActions();
		_trayIcon.addAction(nameChangeAction());
		_trayIcon.addAction(showContactsScreenAction());
		_trayIcon.addAction(sneerPortChangeAction());
		_trayIcon.addAction(new Action() { //Refactor: This action should be moved to a class and the trayicon refresh trigged by a callback  
			public String caption() {
				return translate("Language");
			}

			public void run() {

				Object[] options = { "English", "Português" }; // Implement:detect available languages
				try {
					String choice = (String) _user.choose(translate("Available Languages:"),options);
					if (choice.equals("Português")) {
						Language.load("pt","BR");
					} else {
						Language.reset();
					}
					//Fix: trayicon refresh disabled
					//bindActionsToTrayIcon(); 
				} catch (CancelledByUser cbu) {

				}
			}
		});
		_trayIcon.addAction(exitAction());
	}

	private ShowContactsScreenAction showContactsScreenAction() {
		return new ShowContactsScreenAction(_businessSource.output().contacts(), _businessSource.contactAdder(), _businessSource.contactRemover(), _user,_contactActions);
	}

	private void filloutInitialValues() { // Refactor: remove this logic from the gui. Maybe move to Communicator;
		String ownName = _businessSource.output().ownName().currentValue();
		if (ownName == null || ownName.isEmpty())
			nameChangeAction().run();
	}

	private ValueChangePane sneerPortChangeAction() {
		String prompt=translate(
				" Change this only if you know what you are doing.\n" +
				" Sneer TCP port to listen:");
		return new ValueChangePane(translate("Sneer Port Configuration"), prompt, _user, _businessSource.output().sneerPort(), new IntegerParser(_businessSource.sneerPortSetter()));
	}

	private Action nameChangeAction() {
		String prompt = translate(
				" What is your name?\n" + 
				" (You can change it any time you like)");
		
		return new ValueChangePane(translate("Own Name"),prompt, _user, _businessSource.output().ownName(), _businessSource.ownNameSetter());
	}

	private Action exitAction() {
		return new Action() {

			public String caption() {
				return translate("Exit");
			}

			public void run() {
				System.exit(0);
			}
		};
	}

}
