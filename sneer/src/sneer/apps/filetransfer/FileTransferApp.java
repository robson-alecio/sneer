package sneer.apps.filetransfer;

import static wheel.i18n.Language.translate;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.swing.JFileChooser;

import sneer.apps.asker.Asker;
import sneer.apps.transferqueue.TransferQueue;
import sneer.kernel.appmanager.AppConfig;
import sneer.kernel.appmanager.AppTools;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Packet;
import sneer.kernel.gui.contacts.ContactAction;
import sneer.kernel.pointofview.Contact;
import wheel.io.ui.CancelledByUser;
import wheel.io.ui.User;
import wheel.lang.Omnivore;
import wheel.lang.Threads;

public class FileTransferApp {

	public FileTransferApp(AppConfig config) {
		_user = config._user;
		//_contactAttributes = config._contactAttributes;
		_asker = config._asker;
		_transfer = config._transfer;
		_asker.registerAccepted(FileRequest.class, acceptedCallback());
	}

	private Asker _asker;
	private TransferQueue _transfer;
	private final User _user;
	//private final ListSignal<ContactAttributes> _contactAttributes;
	
	private Omnivore<Packet> acceptedCallback() {
		return new Omnivore<Packet>(){ public void consume(Packet packet) {
			FileRequest request = (FileRequest)packet._contents;
			//Fix: check filename to avoid special chars
			try{
				File directory = chooseTargetDirectory(request._filename);
				if (directory!=null){
					directory.mkdirs();
					File file = new File(directory,request._filename);
					_transfer.receiveFile(packet._contactId, file, request._size, request._transferId, receiverProgressCallback(file.getName()));
				}
			}catch(CancelledByUser cbu){
				//Fix: maybe should notify other side (not obligatory now, works as is)
			}
		}};
	}
	
	protected Omnivore<Long> receiverProgressCallback(final String filename) {
		return new Omnivore<Long>(){ public void consume(Long value) {
			//Refactor: transform into a gui.
			System.out.println("receiving "+filename+" - "+value);
		}};
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
			_asker.ask(contact.id(), callback(contact.id(),file, transferId), new FileRequest(file.getName(),file.length(),transferId));
		}});
	}
	
	private String generateTransferId() { 
		return AppTools.uniqueName("transfer");
	}

	private Omnivore<Boolean> callback(final ContactId contactId, final File file, final String transferId) {
		return new Omnivore<Boolean>(){ public void consume(Boolean accepted) {
			if (accepted)
				_transfer.sendFile(contactId, file, transferId, sendProgressCallback(file.getName()));
		}};
	}

	private Omnivore<Long> sendProgressCallback(final String filename) {
		return new Omnivore<Long>(){ public void consume(Long value) {
			//Refactor: transform into a gui.
			System.out.println("sending "+filename+" - "+value);
		}};
	}

	/*private ContactAttributes findContact(ContactId id) {
		for (ContactAttributes candidate : _contactAttributes)
			if (candidate.id().equals(id)) return candidate;
		return null;
	}*/

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
