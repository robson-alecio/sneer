package sneer.kernel.gui;

import static wheel.i18n.Language.translate;

import java.awt.Font;
import java.io.IOException;
import java.net.URL;

import sneer.SneerDirectories;
import sneer.SystemApplications;
import sneer.kernel.appmanager.gui.AppManagerGui;
import sneer.kernel.business.BusinessSource;
import sneer.kernel.gui.contacts.ActionFactory;
import sneer.kernel.gui.contacts.ShowContactsScreenAction;
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
import wheel.io.ui.impl.JFontChooser;
import wheel.io.ui.impl.JFrameBoundsKeeperImpl;
import wheel.io.ui.impl.TrayIconImpl;
import wheel.io.ui.impl.tests.TransientBoundsPersistence;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.Signal;
import wheel.reactive.lists.ListValueChange;

public class Gui {

	private final ActionFactory _actionFactory;
	private final SystemApplications _systemApplications;

	public Gui(User user, SystemApplications systemApplications, BusinessSource businessSource, ActionFactory actionFactory) throws Exception {
		_user = user;
		_systemApplications = systemApplications;
		_businessSource = businessSource;
		_actionFactory = actionFactory;;
		
		URL icon = Gui.class.getResource("/sneer/kernel/gui/traymenu/yourIconGoesHere.png");
		_trayIcon = new TrayIconImpl(icon, _user.catcher());
		
		_jframeBoundsKeeper = jFrameBoundsKeeper(); 
		tryToRun();
		_systemApplications._appManager.publishedApps().output().addListReceiver(appListChangedReceiver());
	}

	final User _user;
	private final BusinessSource _businessSource;
	private JFrameBoundsKeeper _jframeBoundsKeeper;
	
	private final TrayIcon _trayIcon;

	private ShowContactsScreenAction _showContactsScreenAction;
	
	private void tryToRun() {
		
		filloutInitialValues();
		bindActionsToTrayIcon();
		
		showContactsScreenAction().run();
	}
	
	private Omnivore<ListValueChange> appListChangedReceiver() {
		return new Omnivore<ListValueChange>(){ public void consume(ListValueChange value) {
				bindActionsToTrayIcon();
		}};
	}

	void bindActionsToTrayIcon() {
		ShowContactsScreenAction showContactsScreenAction = showContactsScreenAction();
		_trayIcon.clearActions();
		_trayIcon.setDefaultAction(showContactsScreenAction);
		_trayIcon.addAction(showContactsScreenAction);
		_trayIcon.addAction(languageChangeAction());
		_trayIcon.addAction(showFontScreenAction());
		_trayIcon.addAction(appManagerAction());
		for(Action action:_actionFactory.mainActions())
			_trayIcon.addAction(action);
		_trayIcon.addAction(exitAction());
	}

	private Action showFontScreenAction() {
		return new Action(){
			public String caption() {
				return translate("Font");
			}
			public void run() {
				fontChooser();
			}
		};
	}

	private Action appManagerAction() {
		return new Action() {
			
			public String caption() {
				return translate("App Manager");
			}

			public void run() {
				new AppManagerGui(_systemApplications._appManager);
			}
		};
	}

	private LanguageChangeAction languageChangeAction() {
		return new LanguageChangeAction(_user, _businessSource.output().language(), _businessSource.languageSetter());
	}

	private synchronized ShowContactsScreenAction showContactsScreenAction() {
		if (_showContactsScreenAction == null){
			_showContactsScreenAction = new ShowContactsScreenAction(_user, _systemApplications._me, _actionFactory, _businessSource, _jframeBoundsKeeper);
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
	
	public Signal<Font> font() {
		return _businessSource.output().font();
	}
	

	public void fontChooser() {
		Threads.startDaemon(new Runnable(){
			public void run() {
				JFontChooser chooser = new JFontChooser();
				int option = chooser.showDialog(null, translate("Choose Sneer Font"));
				if (option == JFontChooser.OK_OPTION)
					if (!fontEquals(_businessSource.output().font().currentValue(),chooser.getFont()))
						_businessSource.fontSetter().consume(chooser.getFont());
					
			}
		});
	}

	public boolean fontEquals(Font font1, Font font2){
		return (font1.getName().equals(font2.getName()))&&(font1.getSize()==font2.getSize())&&(font1.getStyle()==font2.getStyle());
	}
	
}
