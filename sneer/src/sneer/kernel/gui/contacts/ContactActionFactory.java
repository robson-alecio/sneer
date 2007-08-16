package sneer.kernel.gui.contacts;

import java.util.ArrayList;
import java.util.List;

import sneer.apps.conversations.ConversationsApp;
import sneer.apps.filetransfer.FileTransferApp;
import sneer.apps.talk.TalkApp;
import sneer.kernel.appmanager.SovereignApplication;
import sneer.kernel.appmanager.AppManager;
import sneer.kernel.business.BusinessSource;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.impl.Communicator;
import sneer.kernel.pointofview.Party;
import wheel.io.ui.JFrameBoundsKeeper;
import wheel.io.ui.User;

public class ContactActionFactory {

	private final User _user;
	private final Communicator _communicator;
	private final BusinessSource _businessSource;
	private final Party _I;
	private final AppManager _appManager;
	private final JFrameBoundsKeeper _jframeBoundsKeeper;

	public ContactActionFactory(User user, Party i, Communicator communicator, BusinessSource businessSource, AppManager appManager, JFrameBoundsKeeper jframeBoundsKeeper){
		_user = user;
		_I = i;
		_communicator = communicator;
		_businessSource = businessSource;
		_appManager = appManager;
		_jframeBoundsKeeper = jframeBoundsKeeper;
		
	}
	
	public List<ContactAction> contactActions(){
		List<ContactAction> result = new ArrayList<ContactAction>();
		Channel conversationsChannel = _communicator.getChannel(ConversationsApp.class.getName(), 0);
		result.add(new ConversationsApp(conversationsChannel, _businessSource.output().contactAttributes(), _user.briefNotifier(), _jframeBoundsKeeper).contactAction());
		
		Channel talkChannel = _communicator.getChannel(TalkApp.class.getName(), 1);
		result.add(new TalkApp(_user, talkChannel, _I.contacts()).contactAction());
		
		Channel fileTransferChannel = _communicator.getChannel(FileTransferApp.class.getName(), 3);
		result.add(new FileTransferApp(_user, fileTransferChannel, _businessSource.output().contactAttributes()).contactAction());
		
		for(SovereignApplication app:_appManager.installedApps().values())
			if (app.contactActions()!=null)
				for(ContactAction contactAction:app.contactActions())
					result.add(contactAction);
		return result;
	}
}
