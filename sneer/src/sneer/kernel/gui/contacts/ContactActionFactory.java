package sneer.kernel.gui.contacts;

import java.util.ArrayList;
import java.util.List;

import sneer.apps.metoo.MeToo;
import sneer.apps.talk.TalkApp;
import sneer.kernel.appmanager.AppManager;
import sneer.kernel.appmanager.SovereignApplicationUID;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.impl.Communicator;
import sneer.kernel.pointofview.Party;
import wheel.io.ui.User;

public class ContactActionFactory {

	private final User _user;
	private final Communicator _communicator;
	private final Party _I;
	private final AppManager _appManager;

	public ContactActionFactory(User user, Party i, Communicator communicator, AppManager appManager){
		_user = user;
		_I = i;
		_communicator = communicator;
		_appManager = appManager;
		initApps();
	}
	
	private MeToo _meToo;
	private TalkApp _talk;
	
	private void initApps() {
		Channel metooChannel = _communicator.getChannel(MeToo.class.getName(), 0);
		Channel talkChannel = _communicator.getChannel(TalkApp.class.getName(), 1);
		_meToo = new MeToo(metooChannel,_appManager.publishedApps().output(), _appManager);
		_talk = new TalkApp(_user, talkChannel, _I.contacts());
	}

	public List<ContactAction> contactActions(){
		List<ContactAction> result = new ArrayList<ContactAction>();
		
		for(ContactAction contactAction:_meToo.contactActions())
			result.add(contactAction);

		result.add(_talk.contactAction());
		
		for(SovereignApplicationUID app:_appManager.publishedApps().output())
			if (app._sovereignApplication.contactActions()!=null)
				for(ContactAction contactAction:app._sovereignApplication.contactActions())
					result.add(contactAction);
		return result;
	}
}
