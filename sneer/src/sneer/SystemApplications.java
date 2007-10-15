package sneer;

import sneer.apps.conversations.Application;
import sneer.apps.publicfiles.PublicFiles;
import sneer.apps.sharedfolder.SharedFolder;
import sneer.apps.transferqueue.TransferQueue;
import sneer.kernel.appmanager.AppManager;
import sneer.kernel.appmanager.metoo.MeToo;
import sneer.kernel.business.BusinessSource;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.impl.Communicator;
import sneer.kernel.pointofview.impl.Me;
import wheel.io.ui.User;
import wheel.io.ui.User.Notification;
import wheel.lang.Omnivore;

public class SystemApplications {

	private final User _user;
	private final Communicator _communicator;
	private final BusinessSource _businessSource;
	public final Me _me;
	public final TransferQueue _transfer;
	public final SharedFolder _sharedFolder;
	public final PublicFiles _publicFiles;
	public final AppManager _appManager;
	private final Omnivore<Notification> _briefNotifier;
	public final Application _conversations;
	public final sneer.apps.filetransfer.Application _fileTransfer;
	public final sneer.apps.talk.Application _talk;
	public final MeToo _meToo;

	//Refactor: make system apps creation more compatible/uniform.
	
	public SystemApplications(User user, Communicator communicator, BusinessSource businessSource, Omnivore<Notification> briefNotifier){
		_user = user;
		_communicator = communicator;
		_businessSource = businessSource;
		_briefNotifier = briefNotifier;
		
		Channel channel = _communicator.openChannel("Point of View", 1);
		_me = new Me(_businessSource.output(), _communicator.operator(), channel);
		
		Channel transferChannel = _communicator.openChannel("Transfer", 2);
		_transfer = new TransferQueue(transferChannel);
		
		Channel sharedFolderChannel = _communicator.openChannel("Shared Folder", 2);
		_sharedFolder = new SharedFolder(sharedFolderChannel, _me.contacts(), _transfer);
		
		Channel publicFilesChannel = _communicator.openChannel("Public Files", 2);
		_publicFiles = new PublicFiles(_user, publicFilesChannel, _me.contacts(), _transfer, _businessSource.output().contactAttributes());
		
		_appManager = new AppManager(_user,_communicator, _me, _businessSource.output().contactAttributes(), _briefNotifier, _transfer);
		
		_conversations = new sneer.apps.conversations.Application();
		_fileTransfer = new sneer.apps.filetransfer.Application();
		_talk = new sneer.apps.talk.Application();
		_appManager.startApp(_conversations);
		_appManager.startApp(_fileTransfer);
		_appManager.startApp(_talk);
		
		// _metoo cant be exposed yet as a normal app without exposing too much appconfig.
		Channel metooChannel = _communicator.openChannel(MeToo.class.getName(), 3);
		_meToo = new MeToo(_user, metooChannel,_appManager.publishedApps().output(), _appManager, _transfer);
		
	}
	

	
	
}
