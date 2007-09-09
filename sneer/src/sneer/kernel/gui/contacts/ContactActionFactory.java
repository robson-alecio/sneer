package sneer.kernel.gui.contacts;

import java.util.ArrayList;
import java.util.List;

import sneer.apps.metoo.MeToo;
import sneer.kernel.appmanager.AppManager;
import sneer.kernel.appmanager.SovereignApplication;
import sneer.kernel.appmanager.SovereignApplicationUID;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.impl.Communicator;
import wheel.io.ui.User;

public class ContactActionFactory {

	private final Communicator _communicator;
	private final AppManager _appManager;
	private final User _user;

	public ContactActionFactory(User user, Communicator communicator, AppManager appManager){
		_user = user;
		_communicator = communicator;
		_appManager = appManager;
		initApps();
	}
	
	private SovereignApplication _conversations;
	private SovereignApplication _fileTransfer;
	private SovereignApplication _talk;
	
	private MeToo _meToo;
	
	private void initApps() {
		
		_conversations = new sneer.apps.conversations.Application();
		_fileTransfer = new sneer.apps.filetransfer.Application();
		_talk = new sneer.apps.talk.Application();
		_appManager.startApp(_conversations, new sneer.apps.conversations.ApplicationInfo());
		_appManager.startApp(_fileTransfer, new sneer.apps.filetransfer.ApplicationInfo());
		_appManager.startApp(_talk, new sneer.apps.talk.ApplicationInfo());
		
		// _metoo cant be exposed yet as a normal app without exposing too much appconfig.
		Channel metooChannel = _communicator.getChannel(MeToo.class.getName(), 3);
		_meToo = new MeToo(_user, metooChannel,_appManager.publishedApps().output(), _appManager);
	}

	public List<ContactAction> contactActions(){
		List<ContactAction> result = new ArrayList<ContactAction>();
		
		registerContactActions(result,_conversations);
		registerContactActions(result,_fileTransfer);
		registerContactActions(result,_talk);
		
		for(ContactAction contactAction:_meToo.contactActions())
			result.add(contactAction);
		
		for(SovereignApplicationUID app:_appManager.publishedApps().output())
			if (app._sovereignApplication.contactActions()!=null)
				for(ContactAction contactAction:app._sovereignApplication.contactActions())
					result.add(contactAction);
		return result;
	}

	private void registerContactActions(List<ContactAction> result, SovereignApplication app) {
		for(ContactAction contactAction:app.contactActions())
			result.add(contactAction);
	}
}
