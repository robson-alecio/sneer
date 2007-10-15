package sneer;

import sneer.kernel.api.SovereignApplication;
import sneer.kernel.api.SovereignApplicationNeeds;
import sneer.kernel.appmanager.AppManager;
import sneer.kernel.appmanager.NeedsImpl;
import sneer.kernel.business.BusinessSource;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.impl.Communicator;
import sneer.kernel.pointofview.impl.Me;
import sneer.kernel.transferqueue.TransferQueue;
import wheel.io.ui.User;
import wheel.io.ui.User.Notification;
import wheel.lang.Omnivore;

public class SystemApplications {

	private final User _user;
	private final Communicator _communicator;
	private final BusinessSource _businessSource;
	private final Omnivore<Notification> _briefNotifier;
	
	public final Me _me;
	public final TransferQueue _transfer;
	public final AppManager _appManager;

	public final sneer.apps.sharedfolder.Application _sharedFolder;
	public final sneer.apps.publicfiles.Application _publicFiles;
	public final sneer.apps.conversations.Application _conversations;
	public final sneer.apps.filetransfer.Application _fileTransfer;
	public final sneer.apps.talk.Application _talk;
	public final sneer.kernel.appmanager.metoo.Application _meToo;

	public SystemApplications(User user, Communicator communicator, BusinessSource businessSource, Omnivore<Notification> briefNotifier){
		_user = user;
		_communicator = communicator;
		_businessSource = businessSource;
		_briefNotifier = briefNotifier;
		
		Channel channel = _communicator.openChannel("Point of View", 1);
		_me = new Me(_businessSource.output(), _communicator.operator(), channel);
		Channel transferChannel = _communicator.openChannel("TransferQueue", 2);
		_transfer = new TransferQueue(transferChannel);
		_appManager = new AppManager(_user,_communicator, _me, _businessSource.output().contactAttributes(), _briefNotifier, _transfer);
		
		_sharedFolder = new sneer.apps.sharedfolder.Application();
		startApp(_sharedFolder);
		
		_publicFiles = new sneer.apps.publicfiles.Application();
		startApp(_publicFiles);

		_conversations = new sneer.apps.conversations.Application();
		startApp(_conversations);
		
		_fileTransfer = new sneer.apps.filetransfer.Application();
		startApp(_fileTransfer);
		
		_talk = new sneer.apps.talk.Application();
		startApp(_talk);
		
		_meToo = new sneer.kernel.appmanager.metoo.Application(_appManager);
		startApp(_meToo);
		
	}
	
	public void startApp(SovereignApplication app){
		SovereignApplicationNeeds needs = new NeedsImpl(_user, _communicator.openChannel(app.defaultName(), app.trafficPriority(), app.getClass().getClassLoader()), _me.contacts(), _businessSource.output().contactAttributes(), _me.name(), _briefNotifier, _transfer);
		app.init(needs);
		app.start();
	}
	
	
}
