package sneer.apps.filetransfer;

import static wheel.i18n.Language.translate;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;

import sneer.apps.asker.Asker;
import sneer.apps.filetransfer.gui.FileTransferFrame;
import sneer.kernel.appmanager.AppConfig;
import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import sneer.kernel.gui.contacts.ContactAction;
import sneer.kernel.pointofview.Contact;
import wheel.io.ui.User;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.impl.SourceImpl;
import wheel.reactive.lists.ListSignal;

public class FileTransferApp {

	private static final int FILEPART_CHUNK_SIZE = 5000;

	public FileTransferApp(AppConfig config) {
		_user = config._user;
		_channel = config._channel;
		_contactAttributes = config._contactAttributes;
		_channel.input().addReceiver(filePartReceiver());
		_asker = config._asker;
	}

	private Asker _asker;
	private final User _user;
	private final Channel _channel;
	private final ListSignal<ContactAttributes> _contactAttributes;
	private final Map<ContactId, FileTransferFrame>_framesByContactId = new HashMap<ContactId, FileTransferFrame>();
	private final Map<ContactId, SourceImpl<FilePart>>_inputsByContactId = new HashMap<ContactId, SourceImpl<FilePart>>();

	private Omnivore<Packet> filePartReceiver() {
		return new Omnivore<Packet>() { public void consume(Packet packet) {
			produceFrameFor(packet._contactId);
			produceInputFor(packet._contactId).setter().consume((FilePart)packet._contents);
		}};
	}
	
	private void actUponContact(final Contact contact) {
		
		Threads.startDaemon(new Runnable() { public void run() {
			final JFileChooser fc = new JFileChooser(); //Refactor: The app should not have GUI logic.
			fc.setDialogTitle(translate("Choose File to Send to %1$s", contact.nick().currentValue()));
			fc.setApproveButtonText(translate("Send"));
			int value = fc.showOpenDialog(null);
			if (value != JFileChooser.APPROVE_OPTION) return;

			File file = fc.getSelectedFile();
			_asker.ask(contact.id(),translate("Can I send you the file %1$s ?",file.getName()), callback(contact.id(),file));
		}});
	}
	
	private Omnivore<Boolean> callback(final ContactId contactId, final File file) {
		return new Omnivore<Boolean>(){ public void consume(Boolean accepted) {
			if (accepted)
				sendFile(contactId, file);
		}};
	}

	private void sendFile(final ContactId contactId, File file) {
		try {
			tryToSendFile(contactId, file);
		} catch(IOException ioe) {
			ioe.printStackTrace(); //Fix: Treat properly.
		}
	}

	private void tryToSendFile(final ContactId contactId, File file) throws IOException {
		String fileName = file.getName();
		long fileLength = file.length();
		byte[] buffer = new byte[FILEPART_CHUNK_SIZE];
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
		int read;
		long offset = 0;
		while ((read = in.read(buffer)) != -1) {
			byte[] contents = new byte[read];
			System.arraycopy(buffer,0,contents,0,read);
			final FilePart filePart = new FilePart(fileName, fileLength, contents, offset);
			sendPart(contactId, filePart);
			offset += read;
		}
	}

	private void sendPart(ContactId contactId, FilePart filePart) {
		_channel.output().consume(new Packet(contactId, filePart));
	}

	private FileTransferFrame produceFrameFor(ContactId contactId) {
		FileTransferFrame frame = _framesByContactId.get(contactId);
		if (frame == null) {
			frame = new FileTransferFrame(_user, findContact(contactId).nick(), inputFrom(contactId));
			_framesByContactId.put(contactId, frame);
		}
		return frame; //Fix: What if this Frame has been closed?
	}

	private ContactAttributes findContact(ContactId id) {
		for (ContactAttributes candidate : _contactAttributes)
			if (candidate.id().equals(id)) return candidate;
		return null;
	}

	private Signal<FilePart> inputFrom(ContactId contactId) {
		return produceInputFor(contactId).output();
	}

	private Source<FilePart> produceInputFor(ContactId contactId) {
		SourceImpl<FilePart> result = _inputsByContactId.get(contactId);
		if (result == null) {
			result = new SourceImpl<FilePart>(null);
			_inputsByContactId.put(contactId, result);
		}
		return result;
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
