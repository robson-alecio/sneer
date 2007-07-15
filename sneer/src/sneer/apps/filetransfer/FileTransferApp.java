package sneer.apps.filetransfer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;

import sneer.apps.filetransfer.gui.FileTransferFrame;
import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import sneer.kernel.gui.contacts.ContactAction;
import static wheel.i18n.Language.*;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.SourceImpl;
import wheel.reactive.lists.ListSignal;

public class FileTransferApp {

	private static final int FILEPART_CHUNK_SIZE = 20000;

	public FileTransferApp(Channel channel, ListSignal<Contact> contacts) {
		_channel = channel;
		_contacts = contacts;
		
		_channel.input().addReceiver(filePartReceiver());
	}

	private final Channel _channel;
	private final ListSignal<Contact> _contacts;
	private final Map<ContactId, FileTransferFrame>_framesByContactId = new HashMap<ContactId, FileTransferFrame>();
	private final Map<ContactId, SourceImpl<FilePart>>_inputsByContactId = new HashMap<ContactId, SourceImpl<FilePart>>();

	public ContactAction contactAction() {
		return new ContactAction(){

			@Override
			public void actUpon(Contact contact) {
				actUponContact(contact);
			}

			@Override
			public String caption() {
				return "Send File";
			}
			
		};
	}

	private Omnivore<Packet> filePartReceiver() {
		return new Omnivore<Packet>() { public void consume(Packet packet) {
			openFrameFor(packet._contactId);
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
			sendFile(contact, file);
		}});
	}
	
	private void sendFile(final Contact contact, File file) {
		try {
			tryToSendFile(contact, file);
		} catch(IOException ioe) {
			ioe.printStackTrace(); //Fix: Treat properly.
		}
	}

	private void tryToSendFile(final Contact contact, File file) throws IOException {
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
			sendPart(contact, filePart);
			offset += read;
		}
	}

	private void sendPart(Contact contact, FilePart filePart) {
		_channel.output().consume(new Packet(contact.id(), filePart));
	}

	private void openFrameFor(ContactId contactId) {
		FileTransferFrame frame = produceFrameFor(contactId);
		frame.setVisible(true);
	}

	private FileTransferFrame produceFrameFor(ContactId contactId) {
		FileTransferFrame frame = _framesByContactId.get(contactId);
		if (frame == null) {
			frame = new FileTransferFrame(findContact(contactId).nick(), inputFrom(contactId));
			_framesByContactId.put(contactId, frame);
		}
		return frame; //Fix: What if this Frame has been closed?
	}

	private Contact findContact(ContactId id) {
		for (Contact candidate : _contacts)
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

}
