package sneer.kernel.appmanager.metoo;
import static wheel.i18n.Language.translate;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import sneer.SneerDirectories;
import sneer.apps.transferqueue.TransferKey;
import sneer.apps.transferqueue.TransferQueue;
import sneer.kernel.appmanager.AppManager;
import sneer.kernel.appmanager.AppTools;
import sneer.kernel.appmanager.SovereignApplicationUID;
import sneer.kernel.appmanager.metoo.gui.MeTooFrame;
import sneer.kernel.appmanager.metoo.packet.AppInstallRequest;
import sneer.kernel.appmanager.metoo.packet.AppListResponse;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import sneer.kernel.gui.contacts.ContactAction;
import sneer.kernel.gui.contacts.DropAction;
import sneer.kernel.pointofview.Contact;
import wheel.io.ui.Action;
import wheel.io.ui.User;
import wheel.lang.Casts;
import wheel.lang.Omnivore;

public class MeToo {
	
	private final Channel _channel;
	private final AppManager _appManager;
	private final TransferQueue _transfer;
	private final User _user;

	public MeToo(User user, Channel channel, AppManager appManager, TransferQueue transfer){
		_user = user;
		_channel = channel;
		_appManager = appManager;
		_transfer = transfer;
		_channel.input().addReceiver(meTooPacketReceiver());
	}

	private Omnivore<Packet> meTooPacketReceiver() {
		return new Omnivore<Packet>(){
			public void consume(Packet packet) {
				MeTooPacket meTooPacket = (MeTooPacket)packet._contents;
				switch(meTooPacket.type()){
					case MeTooPacket.APP_LIST_REQUEST:
						sendAppListResponse(packet._contactId);
						break;
					case MeTooPacket.APP_LIST_RESPONSE:
						updateAppList(packet, meTooPacket);
						break;
					case MeTooPacket.APP_INSTALL_REQUEST:
						sendAppFile(packet._contactId,(AppInstallRequest)meTooPacket);
						break;
				}
			}
		};
	}
	
	private void updateAppList(Packet packet, MeTooPacket meTooPacket) {
		_framesByContactId.get(packet._contactId).updateAppList(((AppListResponse)meTooPacket)._installNameAndSize);
	}
	
	private void sendAppFile(ContactId contactId, AppInstallRequest request) {
		File file = findFile(request._installName);
		if (file==null)
			return;
		sendFile(new TransferKey(request._installName,contactId),file);
	}

	private File findFile(String installName) {
		File appDirectory = new File(SneerDirectories.appsDirectory(),installName);
		if (!appDirectory.exists())
			return null;
		File file = AppTools.findFile(appDirectory,new FilenameFilter(){
			public boolean accept(File dir, String name) {
				return name.equals(AppManager.JAR_NAME);
			}
		});
		return file;
	}
	
	private void sendFile(TransferKey key, File file) {
		_transfer.sendFile(key, file.getAbsolutePath(), sendProgressCallback(file.getName()));
	}

	private Omnivore<Long> sendProgressCallback(final String name) {
		return new Omnivore<Long>(){ public void consume(Long value) {
			System.out.println("Sending :"+name+" - "+ value);
			// not really needed, maybe for logging purposes.
		}};
	}

	protected void sendAppListResponse(ContactId contactId) {
		Map<String,Long> installNameAndSize = new Hashtable<String,Long>();
		for(SovereignApplicationUID app:_appManager.publishedApps().output()){
			File file = findFile(app._installName);
			installNameAndSize.put(app._installName, file.length());
		}
		_channel.output().consume(new Packet(contactId,new AppListResponse(installNameAndSize)));
	}

	public List<ContactAction> contactActions() {
		return Collections.singletonList( (ContactAction)new ContactAction(){
			public void actUpon(Contact contact) {
				openMeTooFrame(contact);
			}
			public String caption() {
				return translate("Applications");
			}
		});
	}
	
	private final Map<ContactId, MeTooFrame> _framesByContactId = new HashMap<ContactId, MeTooFrame>();
	
	protected void openMeTooFrame(Contact contact) {
		if (_framesByContactId.get(contact.id()) == null){
			_framesByContactId.put(contact.id(), new MeTooFrame(_user, _channel, contact, _appManager, _transfer));
		} else {
			_framesByContactId.get(contact.id()).sendAppListRequest();
		}
		_framesByContactId.get(contact.id()).setVisible(true);
	}

	public String defaultName() {
		return "meetoo";
	}

	public List<Action> mainActions() {
		return null;
	}

	public int trafficPriority() {
		return 1;
	}
	
	public List<DropAction> dropActions() {
		return Casts.uncheckedGenericCast(Collections.EMPTY_LIST);
	}

}
