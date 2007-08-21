package sneer.kernel.gui.contacts;

import java.util.ArrayList;
import java.util.List;

import sneer.apps.conversations.ConversationsApp;
import sneer.apps.filetransfer.FileTransferApp;
import sneer.apps.metoo.MeToo;
import sneer.apps.talk.TalkApp;
import sneer.kernel.appmanager.AppManager;
import sneer.kernel.appmanager.SovereignApplicationUID;
import sneer.kernel.business.BusinessSource;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.impl.Communicator;
import sneer.kernel.pointofview.Party;
import wheel.io.ui.User;

public class ContactActionFactory {

	private final User _user;
	private final Communicator _communicator;
	private final BusinessSource _businessSource;
	private final Party _I;
	private final AppManager _appManager;

	public ContactActionFactory(User user, Party i, Communicator communicator, BusinessSource businessSource, AppManager appManager){
		_user = user;
		_I = i;
		_communicator = communicator;
		_businessSource = businessSource;
		_appManager = appManager;
		initApps();
	}
	
	private MeToo _meToo;
	private ConversationsApp _conversations;
	private TalkApp _talk;
	private FileTransferApp _fileTransfer;
	
	private void initApps() {
		Channel metooChannel = _communicator.getChannel(MeToo.class.getName(), 0);
		Channel conversationsChannel = _communicator.getChannel(ConversationsApp.class.getName(), 0);
		Channel talkChannel = _communicator.getChannel(TalkApp.class.getName(), 1);
		Channel fileTransferChannel = _communicator.getChannel(FileTransferApp.class.getName(), 3);
		_meToo = new MeToo(metooChannel,_appManager.publishedApps().output(), _appManager);
		_conversations = new ConversationsApp(conversationsChannel, _businessSource.output().contactAttributes(), _user.briefNotifier());
		_talk = new TalkApp(_user, talkChannel, _I.contacts());
		_fileTransfer = new FileTransferApp(_user, fileTransferChannel, _businessSource.output().contactAttributes());
	}

	public List<ContactAction> contactActions(){
		List<ContactAction> result = new ArrayList<ContactAction>();
		
		for(ContactAction contactAction:_meToo.contactActions())
			result.add(contactAction);

		result.add(_conversations.contactAction());
		result.add(_talk.contactAction());
		result.add(_fileTransfer.contactAction());
		
		for(SovereignApplicationUID app:_appManager.publishedApps().output())
			if (app._sovereignApplication.contactActions()!=null)
				for(ContactAction contactAction:app._sovereignApplication.contactActions())
					result.add(contactAction);
		return result;
	}
}
