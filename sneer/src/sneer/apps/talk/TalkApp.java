package sneer.apps.talk;

import static wheel.i18n.Language.translate;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sneer.apps.talk.gui.TalkFrame;
import sneer.kernel.business.contacts.Contact;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import sneer.kernel.gui.contacts.ContactAction;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.SourceImpl;
import wheel.reactive.lists.ListSignal;

public class TalkApp {

	private static final String OPEN = "Open";
	private static final String CLOSE = "Close";

	public TalkApp(Channel channel, ListSignal<Contact> contacts) {
		_channel = channel;
		_contacts = contacts;
		
		_channel.input().addReceiver(audioPacketReceiver());
	}

	private final Channel _channel;
	private final ListSignal<Contact> _contacts;
	private final Map<ContactId, TalkFrame>_framesByContactId = new HashMap<ContactId, TalkFrame>();
	private final Map<ContactId, SourceImpl<AudioPacket>>_inputsByContactId = new HashMap<ContactId, SourceImpl<AudioPacket>>();

	public ContactAction contactAction() {
		return new ContactAction(){

			@Override
			public void actUpon(Contact contact) {
				actUponContact(contact);
			}

			@Override
			public String caption() {
				return translate("Voice");
			}
			
		};
	}

	private Omnivore<Packet> audioPacketReceiver() {
		return new Omnivore<Packet>() { public void consume(Packet packet) {
			if (OPEN.equals(packet._contents)) {
				open(packet._contactId);
			}
			
			if (CLOSE.equals(packet._contents)) {
				close(packet._contactId);
			}
			
			produceInputFor(packet._contactId).setter().consume((AudioPacket)packet._contents);
		}};
	}
	
	private void close(ContactId contactId) {
		_inputsByContactId.remove(contactId);

		TalkFrame frame = _framesByContactId.remove(contactId);
		if (frame != null) frame.dispose();
	}

	private void open(ContactId contactId) {
		createInputFor(contactId);
		createFrameFor(contactId);
	}

	private void actUponContact(Contact contact) {
		open(contact.id());
		_channel.output().consume(new Packet(contact.id(), OPEN));
	}

	private void createFrameFor(ContactId contactId) {
		TalkFrame frame = new TalkFrame(findContact(contactId).nick(), inputFrom(contactId), outputTo(contactId));
		frame.addWindowListener(closingListener(contactId));
		_framesByContactId.put(contactId, frame);
	}

	private WindowListener closingListener(final ContactId contactId) {
		return new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent ignored) {
				close(contactId);
				_channel.output().consume(new Packet(contactId, CLOSE));
			}
		};
	}

	private Contact findContact(ContactId id) {
		for (Contact candidate : _contacts)
			if (candidate.id().equals(id)) return candidate;
		return null;
	}

	private Signal<AudioPacket> inputFrom(ContactId contactId) {
		return produceInputFor(contactId).output();
	}

	private Source<AudioPacket> produceInputFor(ContactId contactId) {
		SourceImpl<AudioPacket> result = _inputsByContactId.get(contactId);
		return result;
	}

	private void createInputFor(ContactId contactId) {
		SourceImpl<AudioPacket> input = new SourceImpl<AudioPacket>(null);
		_inputsByContactId.put(contactId, input);
	}

	private Omnivore<AudioPacket> outputTo(final ContactId contactId) {
		return new Omnivore<AudioPacket>() { public void consume(AudioPacket audioPacket) {
			_channel.output().consume(new Packet(contactId, audioPacket));
		}};
	}

}
