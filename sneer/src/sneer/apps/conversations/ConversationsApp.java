package sneer.apps.conversations;

import static wheel.i18n.Language.translate;

import java.awt.Rectangle;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sneer.apps.conversations.business.AppPersistenceSource;
import sneer.apps.conversations.gui.ConversationFrame;
import sneer.kernel.api.SovereignApplicationNeeds;
import sneer.kernel.business.contacts.ContactAttributes;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import sneer.kernel.gui.contacts.ContactAction;
import sneer.kernel.pointofview.Contact;
import wheel.io.ui.User.Notification;
import wheel.lang.Omnivore;
import wheel.lang.Pair;
import wheel.reactive.Signal;
import wheel.reactive.Source;
import wheel.reactive.impl.SourceImpl;
import wheel.reactive.lists.ListSignal;

public class ConversationsApp {

	public ConversationsApp(SovereignApplicationNeeds config) {
		_channel = config.channel();
		_contactAttributes = config.contactAttributes();
		_briefUserNotifier = config.briefUserNotifier();
		_channel.input().addReceiver(messageReceiver());
		_persistence = (AppPersistenceSource)config.prevalentState();
	}

	private AppPersistenceSource _persistence;
	private final Channel _channel;
	private final ListSignal<ContactAttributes> _contactAttributes;
	private final Omnivore<Notification> _briefUserNotifier;

	private final Map<ContactId, ConversationFrame>_framesByContactId = new HashMap<ContactId, ConversationFrame>();
	private final Map<ContactId, SourceImpl<Message>>_inputsByContactId = new HashMap<ContactId, SourceImpl<Message>>();

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
		ConversationFrame result = new ConversationFrame(contactId, _persistence,findContact(contactId).nick(), inputFrom(contactId), outputTo(contactId), _briefUserNotifier);
		_framesByContactId.put(contactId, result);
		keepBounds(contactId, result);
		return result;
	}

	private void keepBounds(ContactId contactId, ConversationFrame result) {
		Rectangle bounds = _persistence.output().persistenceFor(contactId).output().bounds().currentValue();
		if (!bounds.equals(result.getBounds()))
			_persistence.boundsSetter().consume(new Pair<ContactId, Rectangle>(contactId,result.getBounds()));
	}

	private ContactAttributes findContact(ContactId id) {
		for (ContactAttributes candidate : _contactAttributes)
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
	
	public List<ContactAction> contactActions() {
		return Collections.singletonList( (ContactAction)new ContactAction() {

			@Override
			public void actUpon(Contact contact) {
				actUponContact(contact);
			}

			@Override
			public String caption() {
				return translate("Messages");
			}

		});
	}

}
