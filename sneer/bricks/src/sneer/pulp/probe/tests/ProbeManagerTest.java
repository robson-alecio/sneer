package sneer.pulp.probe.tests;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Invocation;
import org.jmock.lib.action.CustomAction;
import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.kernel.container.Inject;
import sneer.pulp.connection.ByteConnection;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.connection.ByteConnection.PacketScheduler;
import sneer.pulp.contacts.Contact;
import sneer.pulp.contacts.ContactManager;
import sneer.pulp.distribution.filtering.TupleFilterManager;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.probe.ProbeManager;
import sneer.pulp.serialization.Serializer;
import sneer.pulp.tuples.TupleSpace;
import tests.Contribute;
import tests.JMockContainerEnvironment;
import tests.TestThatIsInjected;
import wheel.lang.Consumer;
import wheel.reactive.impl.Constant;

@RunWith(JMockContainerEnvironment.class)
public class ProbeManagerTest extends TestThatIsInjected {

	@SuppressWarnings("unused")
	@Inject private static ProbeManager _subject;
	@Inject private static TupleFilterManager _filter;
	@Inject private static ContactManager _contactManager;
	@Inject private static TupleSpace _tuples;
	@Inject private static KeyManager _keys;
	
	private final Mockery _mockery = new Mockery();
	
	@Contribute private final ConnectionManager _connectionManager = _mockery.mock(ConnectionManager.class);
	private final ByteConnection _connection = _mockery.mock(ByteConnection.class);
	private PacketScheduler _scheduler;
	@Contribute private final Serializer _serializer = _mockery.mock(Serializer.class);

	@SuppressWarnings("deprecation")
	@Test //(timeout = 10000)
	public void testTupleBlocking() {
		_mockery.checking(new Expectations(){{
			one(_connectionManager).connectionFor(with(aNonNull(Contact.class))); will(returnValue(_connection));
			one(_connection).isOnline(); will(returnValue(new Constant<Boolean>(true)));
			one(_connection).initCommunications(with(aNonNull(PacketScheduler.class)), with(aNonNull(Consumer.class)));
				will(new CustomAction("capturing scheduler") { @Override public Object invoke(Invocation invocation) throws Throwable {
					_scheduler = (PacketScheduler) invocation.getParameter(0);
					return null;
				}});
			allowing(_serializer).serialize(with(aNonNull(TupleWithId.class)));
				will(new CustomAction("serializing tuple id") { @Override public Object invoke(Invocation invocation) throws Throwable {
					TupleWithId _tuple = (TupleWithId) invocation.getParameter(0);
					return new byte[] {(byte)_tuple.id};
				}});

		}});

		Contact neide = _contactManager.addContact("Neide");
		_keys.addKey(neide, _keys.generateMickeyMouseKey("foo"));

		_tuples.acquire(new TupleTypeA(1));
		assertPacketToSend(1);
		_tuples.acquire(new TupleTypeB(2));
		assertPacketToSend(2);

		_filter.block(TupleTypeB.class);
		
		_tuples.acquire(new TupleTypeA(3));
		_tuples.acquire(new TupleTypeB(4));
		assertPacketToSend(3);
		_tuples.acquire(new TupleTypeA(5));
		assertPacketToSend(5);
	}


	private void assertPacketToSend(int id) {
		byte[] packet = _scheduler.highestPriorityPacketToSend();
		_scheduler.previousPacketWasSent();
		assertEquals(id, packet[0]);
	}
	
}
