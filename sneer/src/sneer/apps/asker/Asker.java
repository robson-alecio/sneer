package sneer.apps.asker;

import static wheel.i18n.Language.translate;

import java.util.Hashtable;
import java.util.Map;

import sneer.apps.asker.packet.AskerRequestPacket;
import sneer.apps.asker.packet.AskerRequestPayload;
import sneer.apps.asker.packet.AskerResponse;
import sneer.kernel.business.contacts.ContactId;
import sneer.kernel.communication.Channel;
import sneer.kernel.communication.Packet;
import sneer.kernel.communication.impl.PacketRouter;
import sneer.kernel.communication.impl.Router;
import sneer.kernel.pointofview.Contact;
import wheel.io.ui.User;
import wheel.lang.Omnivore;
import wheel.reactive.lists.ListSignal;

public class Asker {

	private Map<Long,Omnivore<Boolean>> _requestsById = new Hashtable<Long,Omnivore<Boolean>>();
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
		_router.register(AskerResponse.class, acceptReceiver());
		_channel.input().addReceiver(_router);
	}

	private Omnivore<Packet> acceptReceiver() {
		return new Omnivore<Packet>(){ public void consume(Packet packet) {
			AskerResponse response = (AskerResponse)packet._contents;
			if (!response._accepted){
				String nick = findContact(packet._contactId).nick().currentValue();
				_user.modelessAcknowledge(translate("Information"), translate("%1$s denied your request. :(", nick));
			}
			Omnivore<Boolean> request = _requestsById.remove(response._id);
			if (request == null) return; //ignore invalid responses without previous requests
			request.consume(response._accepted);
			
		}};
	}

	private Omnivore<Packet> requestReceiver() {
		return new Omnivore<Packet>(){ public void consume(Packet packet) {		
			AskerRequestPacket requestPacket = (AskerRequestPacket)packet._contents;
			String nick = findContact(packet._contactId).nick().currentValue();
			String prompt = translate("%1$s wants to ask you:\n\n",nick) + requestPacket._payload.prompt();
			_user.confirmWithTimeout(prompt, 10, requestCallback(packet._contactId,requestPacket));
		}};
	}
	
	private Router<AskerRequestPayload> _payloadRouter = new Router<AskerRequestPayload>(null);
	
	public void registerAccepted(Class<?> clazz, Omnivore<AskerRequestPayload> callback){
		_payloadRouter.register(clazz, callback);
	}

	private Omnivore<Boolean> requestCallback(final ContactId contactId, final AskerRequestPacket requestPacket) {
		return new Omnivore<Boolean>(){ public void consume(Boolean accepted) {
			_channel.output().consume(new Packet(contactId,new AskerResponse(requestPacket._id,accepted)));
			if (accepted)
				_payloadRouter.consume(requestPacket._payload);
		}};
	}

	public void ask(ContactId contactId, Omnivore<Boolean> callback, AskerRequestPayload payload){
		long id = generateId();
		_requestsById.put(id,callback);
		_channel.output().consume(new Packet(contactId,new AskerRequestPacket(id,payload)));
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
