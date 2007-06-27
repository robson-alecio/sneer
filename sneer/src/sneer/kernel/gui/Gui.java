package sneer.kernel.gui;

import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static sneer.Language.*;
import sneer.kernel.business.BusinessSource;
import sneer.kernel.gui.contacts.ContactAction;
import sneer.kernel.gui.contacts.ShowContactsScreenAction;
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
		
	}

	void bindActionsToTrayIcon() {
		_trayIcon.clearActions();
		_trayIcon.addAction(nameChangeAction());
		_trayIcon.addAction(new ShowContactsScreenAction(_businessSource.output().contacts(), _businessSource.contactAdder(), _user,_contactActions));
		_trayIcon.addAction(sneerPortChangeAction());
		_trayIcon.addAction(new Action() { //Refactor: This action should be moved to a class and the trayicon refresh trigged by a callback  
			public String caption() {
				return string("LANGUAGESCREEN_CAPTION");
			}

			public void run() {

				Object[] options = { "English", "Português" }; // Implement:detect available languages
				try {
					String choice = (String) _user.choose(string("LANGUAGESCREEN_AVAILABLE_LANGUAGES"),options);
					if (choice.equals("Português")) {
						changeLocale(new Locale("pt", "BR"));
					} else {
						changeLocale(new Locale("en"));
					}
					//Fix: trayicon refresh disabled
					//bindActionsToTrayIcon(); 
				} catch (CancelledByUser cbu) {

				}
			}
		});
		_trayIcon.addAction(exitAction());
	}

	private void filloutInitialValues() { // Refactor: remove this logic from the gui. Maybe move to Communicator;
		String ownName = _businessSource.output().ownName().currentValue();
		if (ownName == null || ownName.isEmpty())
			nameChangeAction().run();
	}

	private ValueChangePane sneerPortChangeAction() {
		return new ValueChangePane(string("TRAYICON_SNEER_PORT_CONFIGURATION"), string("SNEERPORTCHANGE_PROMPT"), _user, _businessSource.output().sneerPort(), new IntegerParser(_businessSource.sneerPortSetter()));
	}

	private Action nameChangeAction() {
		return new ValueChangePane(string("TRAYICON_OWNNAME"),string("NAMECHANGE_PROMPT"), _user, _businessSource.output().ownName(), _businessSource.ownNameSetter());
	}

	private Action exitAction() {
		return new Action() {

			public String caption() {
				return string("TRAYICON_EXIT");
			}

			public void run() {
				System.exit(0);
			}
		};
	}

}
