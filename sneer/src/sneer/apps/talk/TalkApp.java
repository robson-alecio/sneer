package sneer.apps.talk;

import static wheel.i18n.Language.translate;

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
				return translate("Start Talk");
			}
			
		};
	}

	private Omnivore<Packet> audioPacketReceiver() {
		return new Omnivore<Packet>() { public void consume(Packet packet) {
			openFrameFor(packet._contactId);
			produceInputFor(packet._contactId).setter().consume((AudioPacket)packet._contents);
		}};
	}
	
	private void actUponContact(Contact contact) {
		openFrameFor(contact.id());
	}

	private void openFrameFor(ContactId contactId) {
		TalkFrame frame = produceFrameFor(contactId);
		frame.setVisible(true);
	}

	private TalkFrame produceFrameFor(ContactId contactId) {
		TalkFrame frame = _framesByContactId.get(contactId);
		if (frame == null) {
			frame = new TalkFrame(findContact(contactId).nick(), inputFrom(contactId), outputTo(contactId));
			_framesByContactId.put(contactId, frame);
		}
		return frame;
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
		if (result == null) {
			result = new SourceImpl<AudioPacket>(null);
			_inputsByContactId.put(contactId, result);
		}
		return result;
	}

	private Omnivore<AudioPacket> outputTo(final ContactId contactId) {
		return new Omnivore<AudioPacket>() { public void consume(AudioPacket audioPacket) {
			_channel.output().consume(new Packet(contactId, audioPacket));
		}};
	}

}
