package sneer.apps.sharedfolder;

import static wheel.i18n.Language.translate;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sneer.SneerDirectories;
import sneer.apps.sharedfolder.packet.ListOfFilesPacket;
import sneer.apps.sharedfolder.packet.PleaseSendFilePacket;
import sneer.apps.sharedfolder.packet.SharedFolderPacket;
import sneer.apps.transferqueue.TransferKey;
import sneer.apps.transferqueue.TransferQueue;
import sneer.kernel.appmanager.AppTools;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.business.contacts.impl.ContactIdImpl;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import sneer.kernel.gui.contacts.ContactAction;
import sneer.kernel.gui.contacts.DropAction;
import sneer.kernel.pointofview.Contact;
import wheel.io.Log;
import wheel.io.files.impl.FileInfo;
import wheel.io.files.impl.FileManagerAccess;
import wheel.io.files.impl.WindowsAndLinuxCompatibility;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.lists.ListSignal;

public class SharedFolder {

	public SharedFolder(Channel channel, ListSignal<Contact> contacts, TransferQueue transfer){
		_channel = channel;
		_transfer = transfer;
		_channel.input().addReceiver(sharedFolderPacketReceiver());
		_contacts = contacts;
		startDirectoryChecker();
	}
	
	private final Channel _channel;
	private final TransferQueue _transfer;
	private ListSignal<Contact> _contacts;
	
	private Map<ContactId,List<FileInfo>> _listOfFilesByContactId = new HashMap<ContactId,List<FileInfo>>();
	
	private void startDirectoryChecker() {
		Threads.startDaemon(new Runnable(){ public void run() {
			SneerDirectories.sharedFolders().mkdirs();
			while(true){
				Threads.sleepWithoutInterruptions(10000); 
				synchronized(_sharedLock){
					for(Contact contact:_contacts){
						ContactId contactId = contact.id();
						List<FileInfo> files = listAllFiles(contactId);
					
						if (listOfFilesChanged(contactId,files))
							sendListOfFiles(contactId, files);
					
						updateListOfFiles(contactId);
					}
				}
			}
		}});
	}
	
	private void updateListOfFiles(ContactId contactId){
		List<FileInfo> files = listAllFiles(contactId);
		_listOfFilesByContactId.put(contactId, files);
	}
	
	protected boolean listOfFilesChanged(ContactId contactId, List<FileInfo> files) {
		List<FileInfo> last = _listOfFilesByContactId.get(contactId);
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

	private void sendListOfFiles(ContactId contactId, List<FileInfo> files) {
		_channel.output().consume(new Packet(contactId,new ListOfFilesPacket(files.toArray(new FileInfo[0]))));
	}
		
	private List<FileInfo> listAllFiles(ContactId contactId){
		return WindowsAndLinuxCompatibility.listAll(directoryOf(contactId).getAbsolutePath(),false);
	}
	
	private File directoryOf(ContactId contactId){
		File directory = new File(SneerDirectories.sharedFolders(),""+((ContactIdImpl)contactId)._id);
		if (!directory.exists())
			directory.mkdirs();
		return directory;
	}

	private Omnivore<Packet> sharedFolderPacketReceiver() {
		return new Omnivore<Packet>(){ public void consume(Packet packet) {
			SharedFolderPacket sharedFolderPacket = (SharedFolderPacket)packet._contents;
			switch(sharedFolderPacket.type()){
				case SharedFolderPacket.PLEASE_SEND_FILE:
					PleaseSendFilePacket pleaseSend = ((PleaseSendFilePacket)sharedFolderPacket);
					sendRequestedFile(packet._contactId, pleaseSend._transferId, pleaseSend._info);
					break;
				case SharedFolderPacket.LIST_OF_FILES:
					compareSharedFolders(packet._contactId,((ListOfFilesPacket)sharedFolderPacket)._infos);
					break;
			}
		}};
	}

	private void sendRequestedFile(ContactId contactId, String transferId, final FileInfo info) {
		final TransferKey key = new TransferKey(transferId, contactId);
		final File localFile = findFileByName(contactId,info._name);
		Threads.startDaemon(new Runnable(){ public void run() {
			checkIfNotGrowingAndWait(localFile);
			_transfer.sendFile(key, localFile.getAbsolutePath(), sendingProgress(info._name));
		}});
	}

	private File findFileByName(ContactId contactId, String name){
		File targetFile = new File(directoryOf(contactId),name);
		return WindowsAndLinuxCompatibility.normalizedFile(targetFile.getAbsolutePath());
	}
	
	private void checkIfNotGrowingAndWait(File localFile) {
		long firstSize = localFile.length();
		Threads.sleepWithoutInterruptions(2000);
		while (firstSize!=localFile.length()){
			firstSize = localFile.length();
			Threads.sleepWithoutInterruptions(2000);
		}
	}
	
	private Object _sharedLock = new Object();

	private synchronized void compareSharedFolders(ContactId contactId, FileInfo[] remoteInfos) {
		synchronized(_sharedLock){
			List<FileInfo> localInfos = listAllFiles(contactId);
			for(FileInfo remoteInfo:remoteInfos){
				if (!localInfos.contains(remoteInfo))
					requestFile(contactId, remoteInfo);
			}
			for(FileInfo localInfo:localInfos){
				if (!Arrays.asList(remoteInfos).contains(localInfo))
					removeFile(contactId, localInfo);
			}
			updateListOfFiles(contactId);
		}
	}

	private void removeFile(ContactId contactId, FileInfo localInfo) {
		File localFile = findFileByName(contactId,localInfo._name);
		localFile.delete();
	}

	private File touchFile(ContactId contactId, FileInfo remoteInfo) {
		File localFile = findFileByName(contactId,remoteInfo._name);
		try{
			localFile.createNewFile();
		}catch(IOException ioe){
			throw new IllegalStateException(ioe);
		}
		return localFile;
	}

	private void requestFile(ContactId contactId, FileInfo remoteInfo) {
		File target = touchFile(contactId, remoteInfo);
		String transferId = AppTools.uniqueName("transferId"); //Refactor: unify multi purpose random id generators and place in wheel.
		_transfer.receiveFile(new TransferKey(transferId, contactId), target.getAbsolutePath(), remoteInfo._size, receivingProgress(remoteInfo._name));
		_channel.output().consume(new Packet(contactId,new PleaseSendFilePacket(transferId,remoteInfo)));
	}

	private Omnivore<Long> receivingProgress(final String name) {
		return new Omnivore<Long>(){ public void consume(Long value) {
			System.out.println("shared folder receiving: "+name+" - "+value);
		}};
	}
	
	private Omnivore<Long> sendingProgress(final String name) {
		return new Omnivore<Long>(){ public void consume(Long value) {
			System.out.println("shared folder sending: "+name+" - "+value);
		}};
	}

	public List<ContactAction> contactActions() {
		return Collections.singletonList( (ContactAction)new ContactAction() {

			@Override
			public void actUpon(Contact contact) {
				actUponContact(contact);
			}

			@Override
			public String caption() {
				return translate("Shared Folder");
			}

		});
	}

	private void actUponContact(Contact contact) {
		FileManagerAccess.openDirectory(directoryOf(contact.id()));
	}
	
	public List<DropAction> dropActions() {
		return Collections.singletonList( (DropAction)new DropAction() {

			@Override
			public void actUpon(Contact contact,Object object) {
				File file = (File) object;
				try {
					AppTools.copy((File) object, new File(directoryOf(contact.id()),file.getName()));
				} catch (IOException e) {
					Log.log(e);
					e.printStackTrace();
				}
			}

			@Override
			public String caption() {
				return translate("Shared Folder");
			}

			public boolean interested(Object object) {
				if ((object==null)||(!(object instanceof File)))
					return false;
				return true;
			}

		});
	}
	
}
