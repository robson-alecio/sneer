package sneer.pulp.probe.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import sneer.kernel.container.Inject;
import sneer.pulp.connection.ByteConnection;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.connection.ByteConnection.Packet;
import sneer.pulp.connection.ByteConnection.PacketScheduler;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.probe.ProbeManager;
import sneer.pulp.tuples.Tuple;
import sneer.pulp.tuples.TupleSpace;
import wheel.io.serialization.Serializer;
import wheel.io.serialization.impl.XStreamBinarySerializer;
import wheel.lang.Omnivore;
import wheel.lang.Threads;
import wheel.lang.exceptions.NotImplementedYet;
import wheel.reactive.lists.impl.SimpleListReceiver;

public class ProbeManagerImpl implements ProbeManager {


	@Inject
	static private TupleSpace _tuples;

	@Inject
	static private ContactManager _contacts;

	@Inject
	static private ConnectionManager _connections;

	@Inject
	static private KeyManager _keyManager;
	
	
	
	@SuppressWarnings("unused")
	private SimpleListReceiver<Contact> _contactListReceiverToAvoidGC;
	

	private static Serializer _serializer = new XStreamBinarySerializer();
	private static final ClassLoader CLASSLOADER_FOR_TUPLES = TupleSpace.class.getClassLoader();

	{
		registerContactReceiver();
	}

	private void registerContactReceiver() {
		_contactListReceiverToAvoidGC = new SimpleListReceiver<Contact>(_contacts.contacts()){

			@Override
			protected void elementPresent(Contact contact) {
				initCommunications(contact);
			}

			@Override
			protected void elementAdded(Contact contact) {
				initCommunications(contact);
			}

			@Override
			protected void elementToBeRemoved(Contact contact) {
				throw new NotImplementedYet();
			}
			
		};
	}

	private void initCommunications(Contact contact) {
		ByteConnection connection = _connections.connectionFor(contact);
		connection.setReceiver(new Omnivore<byte[]>(){ @Override public void consume(byte[] packet) {
			_tuples.acquire((Tuple)desserialize(packet));
		}});
		connection.setSender(new MyScheduler(contact));
	}

	private Object desserialize(byte[] packet) {
		try {
			return _serializer.deserialize(packet, CLASSLOADER_FOR_TUPLES);
		} catch (ClassNotFoundException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} catch (IOException e) {
			throw new wheel.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}

	
	private static final class MyScheduler implements PacketScheduler, Omnivore<Tuple> {

		private final List<Tuple> _stack = Collections.synchronizedList(new LinkedList<Tuple>());
		private final Contact _contact;
		private PublicKey _contactsPK;
		
		private MyScheduler(Contact contact) {
			_contact = contact;
			_tuples.addSubscription(Tuple.class, this);
		}
		
		@Override
		public Packet highestPriorityPacketToSend() {
			while (_stack.isEmpty())
				Threads.sleepWithoutInterruptions(10);

			synchronized (_stack) {
				int position = _stack.size() - 1;
				return new MyPacket(_stack.get(position), position);
			}
		}
		
		@Override
		public void packetWasSent(Packet packet) {
			_stack.remove(((MyPacket)packet)._position);
		}


		@Override
		public void consume(Tuple tuple) {
			if (!isClearToSend(tuple)) return;
			
			_stack.add(tuple);
		}

		private boolean isClearToSend(Tuple tuple) {
			initContactsPKIfNecessary();
			if (_contactsPK == null) return false;
			
			return !isEcho(tuple);
		}

		private boolean isEcho(Tuple tuple) {
			return _contactsPK.equals(tuple.publisher());
		}

		private void initContactsPKIfNecessary() {
			if (_contactsPK != null) return;
			_contactsPK = _keyManager.keyGiven(_contact);
		}

	}

	private static class MyPacket implements Packet {

		private final int _position;
		private final Tuple _tuple;

		public MyPacket(Tuple tuple, int position) {
			_tuple = tuple;
			_position = position;
		}

		@Override
		public byte[] payload() {
			return _serializer.serialize(_tuple);
		}

	}


}