package sneer.apps.publicfiles;

import static wheel.i18n.Language.translate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import sneer.SneerDirectories;
import sneer.apps.publicfiles.gui.PublicFilesFrame;
import sneer.apps.publicfiles.packet.ListOfFiles;
import sneer.apps.publicfiles.packet.PleaseSendFile;
import sneer.apps.publicfiles.packet.PublicFilesPacket;
import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import sneer.kernel.gui.contacts.ContactAction;
import sneer.kernel.pointofview.Contact;
import sneer.kernel.transferqueue.TransferKey;
import sneer.kernel.transferqueue.TransferQueue;
import wheel.io.files.impl.FileInfo;
import wheel.io.files.impl.FileManagerAccess;
import wheel.io.files.impl.WindowsAndLinuxCompatibility;
import wheel.io.ui.Action;
import wheel.io.ui.User;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.Source;
import wheel.reactive.impl.SourceImpl;
import wheel.reactive.lists.ListSignal;

public class PublicFiles {

	private final Channel _channel;
	private final ListSignal<Contact> _contacts;
	private final TransferQueue _transfer;
	private final ListSignal<ContactAttributes> _contactAttributes;
	private final User _user;

	public PublicFiles(User user, Channel channel, ListSignal<Contact> contacts, TransferQueue transfer, ListSignal<ContactAttributes> contactAttributes){
		_user = user;
		_channel = channel;
		_contacts = contacts;
		_transfer = transfer;
		_contactAttributes = contactAttributes;
		_channel.input().addReceiver(publicFilePacketReceiver());
		startDirectoryChecker();
	}
	
	private void startDirectoryChecker() {
		Threads.startDaemon(new Runnable(){ public void run() {
			SneerDirectories.publicFiles().mkdirs();
			while(true){
				Threads.sleepWithoutInterruptions(10000); 
				List<FileInfo> files = WindowsAndLinuxCompatibility.listAll(SneerDirectories.publicFiles().getAbsolutePath(),false);
				if (listOfFilesChanged(files))
					send(files);
				_listOfFiles = files;
			}
		}});
	}


	private void send(List<FileInfo> files) {
		FileInfo[] infos = files.toArray(new FileInfo[0]);
		for(Contact contact: _contacts)
			_channel.output().consume(new Packet(contact.id(),new ListOfFiles(infos)));
	}


	private List<FileInfo> _listOfFiles = new ArrayList<FileInfo>();

	protected boolean listOfFilesChanged(List<FileInfo> files) {
		List<FileInfo> last = _listOfFiles;
		if (last == null) return true;
		for(FileInfo file:files){
			if (!exists(file, last))
				return true;
		}
		for(FileInfo file:last){
			if (!exists(file, files))
				return true;
		}
		return false;
	}

	private boolean exists(FileInfo file, List<FileInfo> files){
		for(FileInfo temp:files)
			if (temp.equals(file))
				return true;
		return false;
	}
	
	private Map<ContactId, Source<FileInfo[]>> _fileInfosByContactId = new Hashtable<ContactId, Source<FileInfo[]>>();
	
	private Omnivore<Packet> publicFilePacketReceiver() {
		return new Omnivore<Packet>(){ public void consume(Packet packet) {
			PublicFilesPacket publicFilesPacket = (PublicFilesPacket)packet._contents;
			switch(publicFilesPacket.type()){
				case PublicFilesPacket.LIST_OF_FILES:
					produceFileInfosSourceFor(packet._contactId).setter().consume(((ListOfFiles)publicFilesPacket)._infos);
					break;
				case PublicFilesPacket.PLEASE_SEND_FILE:
					PleaseSendFile pleaseSendFile = (PleaseSendFile)publicFilesPacket;
					String filename=SneerDirectories.publicFiles().getAbsolutePath()+pleaseSendFile._info._name;
					_transfer.sendFile(new TransferKey(pleaseSendFile._transferId,packet._contactId), filename, progressCallback(pleaseSendFile._info));
					break;
			}
		}};
	}
	
	protected Omnivore<Long> progressCallback(@SuppressWarnings("unused")
	final FileInfo info) {
		return new Omnivore<Long>(){public void consume(Long value) {
			//System.out.println("Sending public file "+info._name + " - " + value);
			//not really needed, maybe only for logging purposes.
		}};
	}

	private Source<FileInfo[]> produceFileInfosSourceFor(ContactId contactId) {
		Source<FileInfo[]> source = _fileInfosByContactId.get(contactId);
		if (source == null){
			source = new SourceImpl<FileInfo[]>(null);
			_fileInfosByContactId.put(contactId, source);
		}
		return source;
	}
	
	public List<ContactAction> contactActions() {
		return Collections.singletonList( (ContactAction)new ContactAction() {

			@Override
			public void actUpon(Contact contact) {
				actUponContact(contact);
			}

			@Override
			public String caption() {
				return translate("Public Files");
			}

		});
	}

	private void actUponContact(Contact contact) {
		produceFrame(contact.id());
	}
	
	private Map<ContactId, PublicFilesFrame> _framesByContactId = new Hashtable<ContactId,PublicFilesFrame>();
	
	private PublicFilesFrame produceFrame(ContactId contactId) {
		PublicFilesFrame frame = _framesByContactId.get(contactId);
		if (frame == null){
			frame = new PublicFilesFrame(_channel, _transfer,_user, contactId, findContact(contactId).nick(), produceFileInfosSourceFor(contactId).output());
			_framesByContactId.put(contactId, frame);
		}
		return frame;
	}
	
	private ContactAttributes findContact(ContactId id) {
		for (ContactAttributes candidate : _contactAttributes)
			if (candidate.id().equals(id)) return candidate;
		return null;
	}
	
	public List<Action> mainActions() {
		return Collections.singletonList( (Action) new Action() {
			
			public String caption() {
				return translate("Public Files");
			}

			public void run() {
				SneerDirectories.publicFiles().mkdirs();
				FileManagerAccess.openDirectory(SneerDirectories.publicFiles());
			}
		});
	}
	
}
