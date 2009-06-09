package sneer.pulp.probe.impl;

import static sneer.commons.environments.Environments.my;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import sneer.brickness.Tuple;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.connection.ByteConnection;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.pulp.probe.ProbeManager;
import sneer.pulp.reactive.collections.impl.SimpleListReceiver;
import sneer.pulp.serialization.Serializer;
import sneer.pulp.tuples.TupleSpace;

class ProbeManagerImpl implements ProbeManager {
	
	private final TupleSpace _tuples = my(TupleSpace.class);
	private final ContactManager _contacts = my(ContactManager.class);
	private final ConnectionManager _connections = my(ConnectionManager.class);
	private final Serializer _serializer = my(Serializer.class);
	
	@SuppressWarnings("unused")
	private SimpleListReceiver<Contact> _contactListReceiverToAvoidGC;

	private Map<Contact, ProbeImpl> _probesByContact = new HashMap<Contact, ProbeImpl>();
	

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
				_connections.closeConnectionFor(contact);
				_probesByContact.remove(contact);
			}

		};
	}

	private void initCommunications(Contact contact) {
		ByteConnection connection = _connections.connectionFor(contact);
		connection.initCommunications(createProbe(contact, connection)._scheduler, createReceiver());
	}

	private ProbeImpl createProbe(Contact contact, ByteConnection connection) {
		ProbeImpl result = new ProbeImpl(contact, connection.isOnline());
		_probesByContact.put(contact, result);
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
			throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} catch (IOException e) {
			throw new sneer.commons.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}

}