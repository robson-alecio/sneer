package sneer.apps.sharedfolder;

import static wheel.i18n.Language.translate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sneer.SneerDirectories;
import sneer.apps.sharedfolder.packet.FileInfo;
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
import sneer.kernel.pointofview.Contact;
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
	
	private Map<ContactId,List<File>> _listOfFilesByContactId = new HashMap<ContactId,List<File>>();
	
	private void startDirectoryChecker() {
		Threads.startDaemon(new Runnable(){ public void run() {
			SneerDirectories.sharedFolders().mkdirs();
			while(true){
				Threads.sleepWithoutInterruptions(10000); 
				synchronized(_sharedLock){
					for(Contact contact:_contacts){
						ContactId contactId = contact.id();
						List<File> files = listAllFiles(contactId);
					
						if (listOfFilesChanged(contactId,files))
							sendListOfFiles(contactId, files);
					
						_listOfFilesByContactId.put(contactId, files);
					}
				}
			}
		}});
	}
	
	protected boolean listOfFilesChanged(ContactId contactId, List<File> files) {
		List<File> last = _listOfFilesByContactId.get(contactId);
		if (last == null) return true;
		for(File file:files){
			if (!exists(file, last))
				return true;
		}
		for(File file:last){
			if (!exists(file, files))
				return true;
		}
		return false;
	}
	
	private boolean exists(File file, List<File> files){
		for(File temp:files)
			if (normalizePath(temp.getAbsolutePath()).equals(normalizePath(file.getAbsolutePath())))
				return true;
		return false;
	}
	
	private String normalizePath(String path){
		return path.toLowerCase().replace('\\', '/');
	}

	private void sendListOfFiles(ContactId contactId, List<File> files) {
		_channel.output().consume(new Packet(contactId,new ListOfFilesPacket(convertToFileInfos(contactId, files))));
	}
	
	private FileInfo[] convertToFileInfos(ContactId contactId, List<File> files){
		FileInfo[] infos = new FileInfo[files.size()];
		for(int t=0;t<files.size();t++){
			File file = files.get(t);
			infos[t] = new FileInfo(relativeName(contactId, file), file.length());
		}
		return infos;
	}

	private String relativeName(ContactId contactId, File file) {
		return normalizePath(file.getAbsolutePath().substring(directoryOf(contactId).getAbsolutePath().length()));
	}
		
	private List<File> listAllFiles(ContactId contactId){
		List<File> list = new ArrayList<File>();
		AppTools.listFiles(list, directoryOf(contactId));
		return list;
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
			_transfer.sendFile(key, localFile, sendingProgress(info._name));
		}});
	}

	private File findFileByName(ContactId contactId, String name){
		List<File> files = listAllFiles(contactId);
		for(File file:files){
			String path = normalizePath(file.getAbsolutePath());
			String target = normalizePath((new File(directoryOf(contactId),name)).getAbsolutePath());
			if (path.equals(target))
				return file;
		}
		return null;
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
			List<File> localFiles = listAllFiles(contactId);
			FileInfo[] localInfos = convertToFileInfos(contactId, localFiles);
			for(FileInfo remoteInfo:remoteInfos){
				if (!Arrays.asList(localInfos).contains(remoteInfo))
					requestFile(contactId, remoteInfo);
			}
			for(FileInfo localInfo:localInfos){
				if (!Arrays.asList(remoteInfos).contains(localInfo))
					removeFile(contactId, localInfo);
			}
		}
	}

	private void removeFile(ContactId contactId, FileInfo localInfo) {
		File localFile = new File(directoryOf(contactId),localInfo._name);
		localFile.delete();
	}

	private File touchFile(ContactId contactId, FileInfo remoteInfo) {
		File localFile = new File(directoryOf(contactId),remoteInfo._name);
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
		_transfer.receiveFile(new TransferKey(transferId, contactId), target, remoteInfo._size, receivingProgress(remoteInfo._name));
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
	
}
