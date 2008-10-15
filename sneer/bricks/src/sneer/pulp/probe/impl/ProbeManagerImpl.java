package sneer.pulp.probe.impl;

import java.io.IOException;
import java.util.ArrayList;

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
import wheel.reactive.lists.impl.SimpleListReceiver;

public class ProbeManagerImpl implements ProbeManager, Omnivore<Tuple> {

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
		_tuples.addSubscription(Tuple.class, this);
	}

	@Override
	public void consume(Tuple tuple) {
		byte[] packet = _serializer.serialize(tuple);
		for (ByteConnection connection : onlineConnections())
			connection.send(packet);
	}

	private Iterable<ByteConnection> onlineConnections() {
		ArrayList<ByteConnection> result = new ArrayList<ByteConnection>();
		for (Contact contact :_contacts.contacts()) {
			ByteConnection connection = _connections.connectionFor(contact);
			if (connection.isOnline().currentValue()) result.add(connection);
		}
		return result;
	}
	
	private void registerContactReceiver() {
		_contactListReceiverToAvoidGC = new SimpleListReceiver<Contact>(_contacts.contacts()){

			@Override
			protected void elementPresent(Contact contact) {
				receivePacketsFrom(contact);
			}

			@Override
			protected void elementAdded(Contact contact) {
				receivePacketsFrom(contact);
			}

			@Override
			protected void elementToBeRemoved(Contact contact) {
				//My receiver will be naturally garbage collected;
			}
			
		};
	}


	private void receivePacketsFrom(Contact contact) {
		_connections.connectionFor(contact).setReceiver(new Omnivore<byte[]>(){ @Override public void consume(byte[] packet) {
			_tuples.publish((Tuple)desserialize(packet));
		}});
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
