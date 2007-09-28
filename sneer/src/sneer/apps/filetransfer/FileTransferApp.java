package sneer.apps.filetransfer;

import static wheel.i18n.Language.translate;

import java.io.File;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;

import sneer.apps.asker.Asker;
import sneer.apps.filetransfer.gui.FileTransferFrame;
import sneer.apps.transferqueue.TransferKey;
import sneer.apps.transferqueue.TransferQueue;
import sneer.kernel.appmanager.AppConfig;
import sneer.kernel.appmanager.AppTools;
import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Packet;
import sneer.kernel.gui.contacts.ContactAction;
import sneer.kernel.pointofview.Contact;
import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.lists.ListSignal;

public class FileTransferApp {

	public FileTransferApp(AppConfig config) {
		_user = config._user;
		_contactAttributes = config._contactAttributes;
		_asker = config._asker;
		_transfer = config._transfer;
		_asker.registerAccepted(FileRequest.class, acceptedCallback());
	}

	private Asker _asker;
	private TransferQueue _transfer;
	private final User _user;
	private final ListSignal<ContactAttributes> _contactAttributes;
	
	private Omnivore<Packet> acceptedCallback() {
		return new Omnivore<Packet>(){ public void consume(Packet packet) {
			FileRequest request = (FileRequest)packet._contents;
			//Fix: check filename to avoid special chars
			try{
				File directory = chooseTargetDirectory(request._filename);
				if (directory!=null){
					directory.mkdirs();
					File file = new File(directory,request._filename);
					TransferKey key = new TransferKey(request._transferId,packet._contactId);
					_transfer.receiveFile(key, file, request._size, receiverProgressCallback(key, file.getName(), findContact(packet._contactId).nick().currentValue(), request._size));
				}
			}catch(CancelledByUser cbu){
				//Fix: maybe should notify other side (not obligatory now, works as is)
			}
		}};
	}
	
	protected Omnivore<Long> receiverProgressCallback(final TransferKey key, final String filename, final String nick, final long size) {
		return new Omnivore<Long>(){ public void consume(Long value) {
			produceFrameFor(key, false, filename, nick).updateProgressBar(value, size);
			System.out.println("receiving "+filename+" - "+value);
		}};
	}
	
	private Omnivore<Long> sendProgressCallback(final TransferKey key, final String filename, final String nick, final long size) {
		return new Omnivore<Long>(){ public void consume(Long value) {
			produceFrameFor(key, true, filename, nick).updateProgressBar(value, size);
			System.out.println("sending "+filename+" - "+value);
		}};
	}
	
	private Map<TransferKey,FileTransferFrame> _framesByKey = new Hashtable<TransferKey,FileTransferFrame>();
	
	public FileTransferFrame produceFrameFor(TransferKey key, boolean send, String filename, String nick){
		FileTransferFrame frame = _framesByKey.get(key);
		if (frame == null){
			if (send)
				frame = createFrameToSend(filename, nick);
			else
				frame = createFrameToReceive(filename, nick);
			_framesByKey.put(key, frame);
		}
		return frame;
	}
	
	public FileTransferFrame createFrameToSend(String filename, String nick){
		return new FileTransferFrame(translate("Sending file %1$s to %2$s", filename, nick));
	}
	
	public FileTransferFrame createFrameToReceive(String filename, String nick){
		return new FileTransferFrame(translate("Receiving file %1$s from %2$s", filename, nick));
	}

	private File chooseTargetDirectory(String fileName) throws CancelledByUser {
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setApproveButtonText(translate("Receive"));
		fc.setDialogTitle(translate("Receiving %1$s - Choose Download Directory", fileName));
		
		while (true) {
			if (fc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
				throw new CancelledByUser();
		
			File result = fc.getSelectedFile();
			if (!result.isDirectory()) { // User might have entered manually.
				_user.acknowledgeNotification(translate("This is not a valid folder:\n\n%1$s\n\nTry again.", result.getPath()));
				continue;
			}
			return result;
		}
	}


	private void actUponContact(final Contact contact) {
		Threads.startDaemon(new Runnable() { public void run() {
			final JFileChooser fc = new JFileChooser(); //Refactor: The app should not have GUI logic.
			fc.setDialogTitle(translate("Choose File to Send to %1$s", contact.nick().currentValue()));
			fc.setApproveButtonText(translate("Send"));
			int value = fc.showOpenDialog(null);
			if (value != JFileChooser.APPROVE_OPTION) return;

			File file = fc.getSelectedFile();
			String transferId = generateTransferId();
			TransferKey key = new TransferKey(transferId, contact.id());
			_asker.ask(contact.id(), callback(key,file, findContact(contact.id()).nick().currentValue()), new FileRequest(file.getName(),file.length(),transferId));
		}});
	}
	
	private String generateTransferId() { 
		return AppTools.uniqueName("transfer");
	}

	private Omnivore<Boolean> callback(final TransferKey key, final File file, final String nick) {
		return new Omnivore<Boolean>(){ public void consume(Boolean accepted) {
			if (accepted)
				_transfer.sendFile(key, file, sendProgressCallback(key, file.getName(), nick, file.length()));
		}};
	}


	private ContactAttributes findContact(ContactId id) {
		for (ContactAttributes candidate : _contactAttributes)
			if (candidate.id().equals(id)) return candidate;
		return null;
	}

	public List<ContactAction> contactActions() {
		return Collections.singletonList( (ContactAction)new ContactAction() {

			@Override
			public void actUpon(Contact contact) {
				actUponContact(contact);
			}

			@Override
			public String caption() {
				return translate("Send File");
			}

		});
	}

}
