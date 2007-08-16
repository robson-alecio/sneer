package sneer.kernel.gui;

import static wheel.i18n.Language.translate;

import java.io.IOException;
import java.net.URL;

import sneer.SneerDirectories;
import sneer.games.mediawars.mp3sushi.MP3SushiGameApp;
import sneer.kernel.appmanager.AppManager;
import sneer.kernel.appmanager.SovereignApplicationUID;
import sneer.kernel.appmanager.gui.AppManagerGui;
import sneer.kernel.business.BusinessSource;
import sneer.kernel.communication.impl.Communicator;
import sneer.kernel.gui.contacts.ContactActionFactory;
import sneer.kernel.gui.contacts.PlayMp3SushiAction;
import sneer.kernel.gui.contacts.ShowContactsScreenAction;
import sneer.kernel.pointofview.Party;
import wheel.io.Log;
import wheel.io.files.Directory;
import wheel.io.files.impl.DurableDirectory;
import wheel.io.ui.Action;
import wheel.io.ui.BoundsPersistence;
import wheel.io.ui.JFrameBoundsKeeper;
import wheel.io.ui.TrayIcon;
import wheel.io.ui.User;
import wheel.io.ui.ValueChangePane;
import wheel.io.ui.User.Notification;
import wheel.io.ui.impl.DeferredBoundPersistence;
import wheel.io.ui.impl.DirectoryBoundsPersistence;
import wheel.io.ui.impl.JFrameBoundsKeeperImpl;
import wheel.io.ui.impl.TrayIconImpl;
import wheel.io.ui.impl.tests.TransientBoundsPersistence;
import wheel.lang.IntegerParser;
import wheel.lang.Omnivore;

public class Gui {

	private final AppManager _appManager;

	public Gui(User user, Party I, BusinessSource businessSource, Communicator communicator, AppManager appManager) throws Exception {
		_user = user;
		_I = I; 
		_businessSource = businessSource;
		_communicator = communicator;
		_appManager = appManager;
		
		URL icon = Gui.class.getResource("/sneer/kernel/gui/traymenu/yourIconGoesHere.png");
		_trayIcon = new TrayIconImpl(icon, _user.catcher());
		
		_jframeBoundsKeeper = jFrameBoundsKeeper(); 
		
		tryToRun();
	}

	final User _user;
	private final Party _I;
	private final BusinessSource _businessSource;
	private JFrameBoundsKeeper _jframeBoundsKeeper;
	private Communicator _communicator;
	
	private final TrayIcon _trayIcon;

	private ShowContactsScreenAction _showContactsScreenAction;
	private PlayMp3SushiAction _playMp3SushiAction;
	
	private void tryToRun() {
		
		filloutInitialValues();
		bindActionsToTrayIcon();
		
		showContactsScreenAction().run();
	}

	void bindActionsToTrayIcon() {
		ShowContactsScreenAction showContactsScreenAction = showContactsScreenAction();
		_trayIcon.clearActions();
		_trayIcon.setDefaultAction(showContactsScreenAction);
		_trayIcon.addAction(nameChangeAction());
		_trayIcon.addAction(showContactsScreenAction);
		_trayIcon.addAction(playMp3SushiAction());
		_trayIcon.addAction(sneerPortChangeAction());
		_trayIcon.addAction(languageChangeAction());
		_trayIcon.addAction(appManagerAction());
		for(SovereignApplicationUID app : _appManager.publishedApps().output())
			if (app._sovereignApplication.mainActions() != null)
				for(Action mainAction : app._sovereignApplication.mainActions())
					_trayIcon.addAction(mainAction);
		_trayIcon.addAction(exitAction());
	}

	private Action appManagerAction() {
		return new Action() {
			
			public String caption() {
				return translate("App Manager");
			}

			public void run() {
				new AppManagerGui(_appManager);
			}
		};
	}
	
	private LanguageChangeAction languageChangeAction() {
		return new LanguageChangeAction(_user, _businessSource.output().language(), _businessSource.languageSetter());
	}

	private synchronized ShowContactsScreenAction showContactsScreenAction() {
		if (_showContactsScreenAction == null){
			ContactActionFactory contactActionFactory = new ContactActionFactory(_user,_I,_communicator,_businessSource,_appManager,jFrameBoundsKeeper()); //Refactor The gui should not be responsible for creating the actions, just for showing them.
			_showContactsScreenAction = new ShowContactsScreenAction(_user, _I, contactActionFactory, _businessSource.contactAdder2(),_businessSource.contactRemover(), _businessSource.contactNickChanger(), _jframeBoundsKeeper);
		}
		return _showContactsScreenAction;
	}
	
	private JFrameBoundsKeeper jFrameBoundsKeeper() {
		if (_jframeBoundsKeeper != null) return _jframeBoundsKeeper;
		
		BoundsPersistence boundsPersistence;
		try {
			Directory directory = new DurableDirectory(SneerDirectories.sneerDirectory().getPath());
			boundsPersistence = new DeferredBoundPersistence(new DirectoryBoundsPersistence(directory));
		} catch (IOException e) {
			Log.log(e);
			boundsPersistence = new TransientBoundsPersistence();
		}
		return _jframeBoundsKeeper = new JFrameBoundsKeeperImpl(boundsPersistence);
	}

	private PlayMp3SushiAction playMp3SushiAction() {
		if (_playMp3SushiAction == null) 
			_playMp3SushiAction = new PlayMp3SushiAction(_businessSource.output().ownName(), _user, _communicator.getChannel(MP3SushiGameApp.class.getName(), 0),_businessSource.output().contactAttributes());
		return _playMp3SushiAction;
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
		return new ValueChangePane(translate("Sneer Port"), prompt, _user, _businessSource.output().sneerPort(), new IntegerParser(_businessSource.sneerPortSetter()));
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

	public Omnivore<Notification> briefNotifier() {
		return new Omnivore<Notification>() { @Override public void consume(Notification notification) {
			_trayIcon.messageBalloon(notification._title, notification._notification);
		}};
	}
	
}
