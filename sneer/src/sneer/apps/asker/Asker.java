package sneer.apps.asker;

import static wheel.i18n.Language.translate;

import java.util.Hashtable;
import java.util.Map;

import sneer.apps.asker.packet.AskerAcceptResponse;
import sneer.apps.asker.packet.AskerDenyResponse;
import sneer.apps.asker.packet.AskerPacket;
import sneer.apps.asker.packet.AskerRequestPacket;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import sneer.kernel.pointofview.Contact;
import wheel.io.ui.User;
import wheel.lang.Omnivore;
import wheel.reactive.lists.ListSignal;

public class Asker {

	private Map<Long,AskerRequest> _requestsById = new Hashtable<Long,AskerRequest>();
	private final User _user;
	private final Channel _channel;
	private final ListSignal<Contact> _contacts;
	private PacketRouter _router = new PacketRouter(null);
	private long _idGenerator;
	
	public Asker(User user, Channel channel, ListSignal<Contact> contacts){
		_user = user;
		_channel = channel;
		_contacts = contacts;
		_router.register(AskerRequestPacket.class, requestReceiver());
		_router.register(AskerAcceptResponse.class, acceptReceiver());
		_router.register(AskerDenyResponse.class, denyReceiver());
		_channel.input().addReceiver(_router);
	}

	private Omnivore<Packet> denyReceiver() {
		return new Omnivore<Packet>(){ public void consume(Packet packet) {
			String nick = findContact(packet._contactId).nick().currentValue();
			_user.modelessAcknowledge(translate("Information"), translate("%1$s denied your request. :(", nick));
			consumePacket(packet,false);
		}};
	}

	private Omnivore<Packet> acceptReceiver() {
		return new Omnivore<Packet>(){ public void consume(Packet packet) {
			consumePacket(packet,true);
		}};
	}
		
	private void consumePacket(Packet packet, boolean state) {
			AskerPacket askerPacket = (AskerPacket)packet._contents;
			AskerRequest request = _requestsById.remove(askerPacket._id);
			if (request == null) return; //ignore invalid responses without previous requests
			request._callback.consume(state);
	};

	private Omnivore<Packet> requestReceiver() {
		return new Omnivore<Packet>(){ public void consume(Packet packet) {		
			AskerRequestPacket requestPacket = (AskerRequestPacket)packet._contents;
			String nick = findContact(packet._contactId).nick().currentValue();
			String prompt = translate("%1$s wants to ask you:\n\n %2$s", nick, requestPacket._message);
			_user.confirmWithTimeout(prompt, 10, requestCallback(packet._contactId,requestPacket._id));
		}};
	}

	private Omnivore<Boolean> requestCallback(final ContactId contactId, final long id) {
		return new Omnivore<Boolean>(){ public void consume(Boolean accepted) {
			if (accepted)
				_channel.output().consume(new Packet(contactId,new AskerAcceptResponse(id)));
			else
				_channel.output().consume(new Packet(contactId,new AskerDenyResponse(id)));	
		}};
	}

	public void ask(ContactId contactId, String message, Omnivore<Boolean> callback){
		long id = generateId();
		_requestsById.put(id,new AskerRequest(message, callback));
		_channel.output().consume(new Packet(contactId,new AskerRequestPacket(id,message)));
	}

	private synchronized Long generateId() {
		return _idGenerator++;
	}
	
	private Contact findContact(ContactId id) {
		for (Contact candidate : _contacts)
			if (candidate.id().equals(id))
				return candidate;
		return null;
	}
}
