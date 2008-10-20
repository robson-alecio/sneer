package sneer.pulp.probe.impl;

import java.io.IOException;

import sneer.kernel.container.Inject;
import sneer.pulp.connection.ByteConnection;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.connection.ByteConnection.Sender;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
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
			_tuples.publish((Tuple)desserialize(packet));
		}});
		connection.setSender(new MySender());
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

	
	private final class MySender implements Sender {
		private long _nextTupleToSend = 0;

		@Override
		public void currentPacketSent() {
			_nextTupleToSend++;
		}

		@Override
		public byte[] currentPacketToSend() {
			while (_nextTupleToSend >= _tuples.tupleCount())
				Threads.sleepWithoutInterruptions(10); //Optimize Use wait/notify.

			while (true) {
				Tuple result = _tuples.tuple(_nextTupleToSend);
				if (result != null)
					return _serializer.serialize(result);
				_nextTupleToSend++;					
			}
		}
	}

}