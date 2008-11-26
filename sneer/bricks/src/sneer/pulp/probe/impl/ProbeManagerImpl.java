package sneer.pulp.probe.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import sneer.kernel.container.Inject;
import sneer.pulp.connection.ByteConnection;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.pulp.probe.ProbeManager;
import sneer.pulp.serialization.Serializer;
import sneer.pulp.tuples.Tuple;
import sneer.pulp.tuples.TupleSpace;
import wheel.lang.Consumer;
import wheel.lang.exceptions.NotImplementedYet;
import wheel.reactive.lists.impl.SimpleListReceiver;

public class ProbeManagerImpl implements ProbeManager {


	@Inject static private TupleSpace _tuples;
	@Inject static private ContactManager _contacts;
	@Inject static private ConnectionManager _connections;
	@Inject private static Serializer _serializer;
	
	
	@SuppressWarnings("unused")
	private SimpleListReceiver<Contact> _contactListReceiverToAvoidGC;

	private Set<ProbeImpl> _probes = new HashSet<ProbeImpl>();
	

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
			protected void elementRemoved(Contact contact) {
				throw new NotImplementedYet();
			}
		};
	}

	private void initCommunications(Contact contact) {
		ByteConnection connection = _connections.connectionFor(contact);
		connection.initCommunications(createProbe(contact, connection)._scheduler, createReceiver());
	}

	private ProbeImpl createProbe(Contact contact, ByteConnection connection) {
		ProbeImpl result = new ProbeImpl(contact, connection.isOnline());
		_probes.add(result);
		return result;
	}

	private Consumer<byte[]> createReceiver() {
		return new Consumer<byte[]>(){ @Override public void consume(byte[] packet) {
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