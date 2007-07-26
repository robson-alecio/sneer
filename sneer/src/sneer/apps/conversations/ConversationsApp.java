package sneer.apps.conversations;

import static wheel.i18n.Language.translate;

import java.util.HashMap;
import java.util.Map;

import sneer.apps.conversations.gui.ConversationFrame;
import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import sneer.kernel.gui.contacts.ContactAction;
import sneer.kernel.pointofview.Contact;
import wheel.io.ui.JFrameBoundsKeeper;
import wheel.io.ui.User.Notification;
import wheel.lang.Omnivore;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.impl.SourceImpl;
import wheel.reactive.lists.ListSignal;

public class ConversationsApp {


	private final JFrameBoundsKeeper _boundsKeeper;

	public ConversationsApp(Channel channel, ListSignal<ContactAttributes> contacts, Omnivore<Notification> briefUserNotifier, JFrameBoundsKeeper boundsKeeper) {
		_channel = channel;
		_contacts = contacts;
		_briefUserNotifier = briefUserNotifier;
		_boundsKeeper = boundsKeeper;
		
		_channel.input().addReceiver(messageReceiver());
	}

	private final Channel _channel;
	private final ListSignal<ContactAttributes> _contacts;
	private final Omnivore<Notification> _briefUserNotifier;

	private final Map<ContactId, ConversationFrame>_framesByContactId = new HashMap<ContactId, ConversationFrame>();
	private final Map<ContactId, SourceImpl<Message>>_inputsByContactId = new HashMap<ContactId, SourceImpl<Message>>();

	public ContactAction contactAction() {
		return new ContactAction(){

			@Override
			public void actUpon(Contact contact) {
				actUponContact(contact);
			}

			@Override
			public String caption() {
				return translate("Messages");
			}
			
		};
	}

	private Omnivore<Packet> messageReceiver() {
		return new Omnivore<Packet>() { public void consume(Packet packet) {
			openFrameFor(packet._contactId);
			produceInputFor(packet._contactId).setter().consume((Message)packet._contents);
		}};
	}
	
	private void actUponContact(Contact contact) {
		openFrameFor(contact.id());
	}

	private void openFrameFor(ContactId contactId) {
		ConversationFrame frame = produceFrameFor(contactId);
		frame.setVisible(true);
	}

	private ConversationFrame produceFrameFor(ContactId contactId) {
		ConversationFrame frame = _framesByContactId.get(contactId);
		if (frame != null) return frame;
		
		return createFrame(contactId);
	}

	private ConversationFrame createFrame(ContactId contactId) {
		ConversationFrame result = new ConversationFrame(findContact(contactId).nick(), inputFrom(contactId), outputTo(contactId), _briefUserNotifier);
		_framesByContactId.put(contactId, result);
		_boundsKeeper.keepBoundsFor(result, ConversationFrame.class.getName() + "/" + contactId);
		return result;
	}

	private ContactAttributes findContact(ContactId id) {
		for (ContactAttributes candidate : _contacts)
			if (candidate.id().equals(id)) return candidate;
		return null;
	}

	private Signal<Message> inputFrom(ContactId contactId) {
		return produceInputFor(contactId).output();
	}

	private Source<Message> produceInputFor(ContactId contactId) {
		SourceImpl<Message> result = _inputsByContactId.get(contactId);
		if (result == null) {
			result = new SourceImpl<Message>(null);
			_inputsByContactId.put(contactId, result);
		}
		return result;
	}

	private Omnivore<Message> outputTo(final ContactId contactId) {
		return new Omnivore<Message>() { public void consume(Message message) {
			_channel.output().consume(new Packet(contactId, message));
		}};
	}

}
