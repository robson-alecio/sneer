package sneer.pulp.probe.tests;

import static sneer.brickness.Environments.my;

import org.jmock.Expectations;
import org.jmock.api.Invocation;
import org.jmock.lib.action.CustomAction;
import org.junit.Ignore;
import org.junit.Test;

import sneer.pulp.bandwidth.BandwidthCounter;
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
import tests.TestInContainerEnvironment;
import wheel.lang.Consumer;
import wheel.reactive.impl.Constant;
import wheel.testutil.SignalUtils;

public class ProbeManagerTest extends TestInContainerEnvironment {

	@Contribute private final ConnectionManager _connectionManager = mock(ConnectionManager.class);
	@Contribute private final Serializer _serializer = mock(Serializer.class);
	@Contribute private final BandwidthCounter _bandwidthCounter = mock(BandwidthCounter.class);

	@SuppressWarnings("unused")
	private final ProbeManager _subject = my(ProbeManager.class);
	private final TupleFilterManager _filter = my(TupleFilterManager.class);
	private final ContactManager _contactManager = my(ContactManager.class);
	private final TupleSpace _tuples = my(TupleSpace.class);
	private final KeyManager _keys = my(KeyManager.class);
	
	private final ByteConnection _connection = mock(ByteConnection.class);
	private PacketScheduler _scheduler;
	@SuppressWarnings("unused")
	private Consumer<byte[]> _packetReceiver;

	@SuppressWarnings("deprecation") //mickeyMouseKey()
	@Test (timeout = 1000)
	public void testTupleBlocking() {
		checking(new Expectations(){{
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

	@SuppressWarnings("deprecation") //mickeyMouseKey()
	@Test (timeout = 1000)
	@Ignore
	public void testBandwidthReporting() {
		checking(new Expectations(){{
			one(_connectionManager).connectionFor(with(aNonNull(Contact.class))); will(returnValue(_connection));
			one(_connection).isOnline(); will(returnValue(new Constant<Boolean>(true)));
			one(_connection).initCommunications(with(aNonNull(PacketScheduler.class)), with(aNonNull(Consumer.class)));
				will(new CustomAction("capturing params") { @Override public Object invoke(Invocation invocation) throws Throwable {
					_scheduler = (PacketScheduler) invocation.getParameter(0);
					_packetReceiver = (Consumer<byte[]>)invocation.getParameter(1);
					return null;
				}});
			one(_bandwidthCounter).sent(1024);
			allowing(_serializer).serialize(with(aNonNull(TupleWithId.class)));
				will(new CustomAction("serializing") { @Override public Object invoke(Invocation invocation) throws Throwable {
					return new byte[1024];
				}});

		}});

		Contact neide = _contactManager.addContact("Neide");
		_keys.addKey(neide, _keys.generateMickeyMouseKey("foo"));

		_tuples.acquire(new TupleTypeA(1));
		SignalUtils.waitForValue(1, _bandwidthCounter.uploadSpeed());
		
		
		_tuples.acquire(new TupleTypeB(2));
		assertPacketToSend(2);

		_filter.block(TupleTypeB.class);
		
		_tuples.acquire(new TupleTypeA(3));
		_tuples.acquire(new TupleTypeB(4));
		assertPacketToSend(3);
		_tuples.acquire(new TupleTypeA(5));
		assertPacketToSend(5);
	}

	
}
