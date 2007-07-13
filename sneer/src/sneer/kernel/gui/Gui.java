package sneer.kernel.gui;

import static wheel.i18n.Language.translate;

import java.net.URL;
import java.util.List;

import sneer.kernel.business.BusinessSource;
import sneer.kernel.gui.contacts.ContactAction;
import sneer.kernel.gui.contacts.ShowContactsScreenAction;
import wheel.io.ui.TrayIcon;
import wheel.io.ui.User;
import wheel.io.ui.Util;
import wheel.io.ui.ValueChangePane;
import wheel.io.ui.TrayIcon.Action;
import wheel.io.ui.impl.TrayIconImpl;
import wheel.lang.IntegerParser;

public class Gui {

	public Gui(User user, BusinessSource businessSource, List<ContactAction> contactActions) throws Exception {
		_user = user;
		_businessSource = businessSource;
		_contactActions = contactActions;

		URL icon = Gui.class.getResource("/sneer/kernel/gui/traymenu/yourIconGoesHere.png");
		_trayIcon = new TrayIconImpl(icon, _user.catcher());

		tryToRun();
	}

	final User _user;

	private final TrayIcon _trayIcon;

	final BusinessSource _businessSource;

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
		_trayIcon.addAction(languageChangeAction());
		_trayIcon.addAction(exitAction());
	}

	private LanguageChangeAction languageChangeAction() {
		return new LanguageChangeAction(_user, _businessSource.output().language(), _businessSource.languageSetter());
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
				"Change this only if you know what you are doing.\n" +
				"Sneer TCP port to listen:");
		return new ValueChangePane(translate("Sneer Port Configuration"), prompt, _user, _businessSource.output().sneerPort(), new IntegerParser(_businessSource.sneerPortSetter()));
	}

	private Action nameChangeAction() {
		String prompt = translate(
				"What is your name?\n" + 
				"(You can change it any time you like)");
		
		return new ValueChangePane(translate("Own Name"), prompt, _user, _businessSource.output().ownName(), _businessSource.ownNameSetter());
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
