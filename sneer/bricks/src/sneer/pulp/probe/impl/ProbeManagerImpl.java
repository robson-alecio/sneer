package sneer.pulp.probe.impl;

import java.io.IOException;

import sneer.kernel.container.Inject;
import sneer.pulp.connection.ByteConnection;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.pulp.probe.ProbeManager;
import sneer.pulp.tuples.Tuple;
import sneer.pulp.tuples.TupleSpace;
import wheel.io.serialization.Serializer;
import wheel.io.serialization.impl.XStreamBinarySerializer;
import wheel.lang.Omnivore;
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

			@Override
			public void elementInserted(int index, Contact value) {
				throw new wheel.lang.exceptions.NotImplementedYet(); // Implement
			}
			
		};
	}

	private void initCommunications(Contact contact) {
		ByteConnection connection = _connections.connectionFor(contact);
		connection.initCommunications(new SchedulerImpl(contact), createReceiver());
	}

	private Omnivore<byte[]> createReceiver() {
		return new Omnivore<byte[]>(){ @Override public void consume(byte[] packet) {
			_tuples.acquire((Tuple)desserialize(packet));
		}};
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


}