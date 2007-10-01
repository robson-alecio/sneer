package sneer.apps.talk;

import static wheel.i18n.Language.translate;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sneer.apps.asker.Asker;
import sneer.apps.talk.gui.TalkFrame;
import sneer.kernel.api.SovereignApplicationNeeds;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import sneer.kernel.gui.contacts.ContactAction;
import sneer.kernel.pointofview.Contact;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.impl.SourceImpl;
import wheel.reactive.lists.ListSignal;

public class TalkApp {

	private static final String CLOSE = "Close";
	private Asker _asker;

	public TalkApp(SovereignApplicationNeeds config) {
		_channel = config.channel();
		_contacts = config.contacts();
		_asker = config.asker();
		_channel.input().addReceiver(audioPacketReceiver());
		_asker.registerAccepted(AudioRequest.class, acceptedCallback());
	}

	private Omnivore<Packet> acceptedCallback() {
		return new Omnivore<Packet>(){ public void consume(Packet packet) {
			//AudioRequest request = (AudioRequest)packet._contents;
			open(packet._contactId);
			System.out.println("Talk from "+findContact(packet._contactId).nick()+" accepted.");
		}};
	}

	private final Channel _channel;
	private final ListSignal<Contact> _contacts;
	private final Map<ContactId, TalkFrame>_framesByContactId = new HashMap<ContactId, TalkFrame>();
	private final Map<ContactId, SourceImpl<AudioPacket>>_inputsByContactId = new HashMap<ContactId, SourceImpl<AudioPacket>>();

	public List<ContactAction> contactActions() {
		return Collections.singletonList((ContactAction)new ContactAction(){

			@Override
			public void actUpon(Contact contact) {
				_asker.ask(contact.id(), callback(contact.id()), new AudioRequest());
			}

			@Override
			public String caption() {
				return translate("Voice");
			}
			
		});
	}

	private Omnivore<Packet> audioPacketReceiver() {
		return new Omnivore<Packet>() { public void consume(Packet packet) {
			
			if (CLOSE.equals(packet._contents)) {
				close(packet._contactId);
				return;
			}
			
			Source<AudioPacket> input = getInputFor(packet._contactId);
			if (input == null) return;
			input.setter().consume((AudioPacket)packet._contents);
		}};
	}

	private void close(ContactId contactId) {
		_inputsByContactId.remove(contactId);

		TalkFrame frame = _framesByContactId.remove(contactId);
		if (frame != null) frame.close();
	}

	private void open(ContactId contactId) {
		createInputFor(contactId);
		createFrameFor(contactId);
		findContact(contactId).party().isOnline().addReceiver(offlineCloser(contactId));
	}

	private Omnivore<Boolean> offlineCloser(final ContactId contactId) {
		return new Omnivore<Boolean>() { @Override public void consume(Boolean isOnline) {
			if (!isOnline) close(contactId);
		}};
	}

	private Omnivore<Boolean> callback(final ContactId contactId) {
		return new Omnivore<Boolean>(){ public void consume(Boolean accepted) {
			if (accepted){
				if (getInputFor(contactId) != null) return;
				open(contactId);
			}	
		}};
	}

	private void createFrameFor(ContactId contactId) {
		TalkFrame frame = new TalkFrame(findContact(contactId).nick(), inputFrom(contactId), outputTo(contactId), _channel.elementsInInputBuffer());
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
		return getInputFor(contactId).output();
	}

	private Source<AudioPacket> getInputFor(ContactId contactId) {
		return _inputsByContactId.get(contactId);
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
