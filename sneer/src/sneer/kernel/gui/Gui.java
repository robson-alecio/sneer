package sneer.kernel.gui;

import static wheel.i18n.Language.translate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.swing.JFileChooser;

import sneer.SneerDirectories;
import sneer.kernel.appmanager.AppManager;
import sneer.kernel.appmanager.SovereignApplicationUID;
import sneer.kernel.appmanager.gui.AppManagerGui;
import sneer.kernel.business.BusinessSource;
import sneer.kernel.gui.contacts.ContactActionFactory;
import sneer.kernel.gui.contacts.ShowContactsScreenAction;
import sneer.kernel.pointofview.Party;
import wheel.graphics.JpgImage;
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
	private final ContactActionFactory _contactActionFactory;

	public Gui(User user, Party I, BusinessSource businessSource, AppManager appManager, ContactActionFactory contactActionFactory) throws Exception {
		_user = user;
		_I = I; 
		_businessSource = businessSource;
		_appManager = appManager;
		_contactActionFactory = contactActionFactory;
		
		URL icon = Gui.class.getResource("/sneer/kernel/gui/traymenu/yourIconGoesHere.png");
		_trayIcon = new TrayIconImpl(icon, _user.catcher());
		
		_jframeBoundsKeeper = jFrameBoundsKeeper(); 
		tryToRun();
	}

	final User _user;
	private final Party _I;
	private final BusinessSource _businessSource;
	private JFrameBoundsKeeper _jframeBoundsKeeper;
	
	private final TrayIcon _trayIcon;

	private ShowContactsScreenAction _showContactsScreenAction;
	
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
		_trayIcon.addAction(thoughtOfTheDayChangeAction());
		_trayIcon.addAction(pictureChangeAction());
		_trayIcon.addAction(profileChangeAction());
		_trayIcon.addAction(showContactsScreenAction);
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
			_showContactsScreenAction = new ShowContactsScreenAction(_user, _I, _contactActionFactory, _businessSource, _jframeBoundsKeeper);
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

	private void filloutInitialValues() { // Refactor: remove this logic from the gui. Maybe move to Communicator;
		String ownName = _businessSource.output().ownName().currentValue();
		if (ownName == null || ownName.isEmpty())
			nameChangeAction().run();
		String thoughtOfTheDay = _businessSource.output().thoughtOfTheDay().currentValue();
		if (thoughtOfTheDay == null || thoughtOfTheDay.isEmpty())
			thoughtOfTheDayChangeAction().run();
		String profile = _businessSource.output().profile().currentValue();
		if (profile == null || profile.isEmpty())
			profileChangeAction().run();
		JpgImage picture = _businessSource.output().picture().currentValue();
		if (picture == null){
			setPicture(Gui.class.getResourceAsStream(IMAGE_PATH + "questionmark.jpg"));
			boolean confirmed = _user.confirm(translate("Do you want to choose a picture now?"));
			if (!confirmed) return;
			pictureChangeAction().run();
		}
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
	
	private Action thoughtOfTheDayChangeAction() {
		String prompt = translate(
				"What is your Thought Of The Day?\n" + 
				"(You can change it any time you like)");
		
		return new ValueChangePane(translate("Thought Of The Day"), prompt, _user, _businessSource.output().thoughtOfTheDay(), _businessSource.thoughtOfTheDaySetter());
	}
	
	private Action profileChangeAction() {
		String prompt = translate(
				"What is your Profile?\n" + 
				"(You can change it any time you like)");
		
		return new ValueChangePane(translate("Profile"), prompt, _user, _businessSource.output().profile(), _businessSource.profileSetter());
	}
	
	private Action pictureChangeAction() {
		return new Action(){

			public String caption() {
				return translate("Picture");
			}

			public void run() {
				try{
					final JFileChooser fc = new JFileChooser(); //Refactor: this should be moved to _user and apps should use the same system to choose a file
					fc.setDialogTitle(translate("Choose a Picture"));
					fc.setApproveButtonText(translate("Use"));
					int value = fc.showOpenDialog(null);
					if (value != JFileChooser.APPROVE_OPTION) return;
					File file = fc.getSelectedFile();
					setPicture(new FileInputStream(file));
				}catch(Exception ignored){
					
				}
			}
			
		};
	}
	
	final static String IMAGE_PATH = "/sneer/kernel/gui/contacts/images/";
	
	private void setPicture(InputStream input) {
		try{
			_businessSource.pictureSetter().consume(new JpgImage(input));
		}catch(Exception ignored){
		}
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
