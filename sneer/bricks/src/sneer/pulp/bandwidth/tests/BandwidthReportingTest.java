package sneer.pulp.bandwidth.tests;

import static sneer.commons.environments.Environments.my;

import org.junit.Test;

import sneer.brickness.testsupport.BrickTest;
import sneer.brickness.testsupport.Contribute;
import sneer.commons.lang.ByRef;
import sneer.hardware.cpu.lang.Consumer;
import sneer.pulp.bandwidth.BandwidthCounter;
import sneer.pulp.connection.ByteConnection;
import sneer.pulp.connection.ConnectionManager;
import sneer.pulp.connection.ByteConnection.PacketScheduler;
import sneer.pulp.contacts.Contact;
import sneer.pulp.lang.StringUtils;
import sneer.pulp.network.ByteArraySocket;
import sneer.pulp.reactive.Signal;
import sneer.pulp.reactive.Signals;
import sneer.pulp.threads.mocks.ThreadsMock;

public class BandwidthReportingTest extends BrickTest {
	
	private static final byte[] _BYTE_ARRAY_OK = my(StringUtils.class).toByteArray("OK");
	private static final int _IN_PACKET_SIZE = _BYTE_ARRAY_OK.length;
	private static final int _OUT_PACKET_SIZE = 1024;
	
	@Contribute private final ThreadsMock _threads = new ThreadsMock();
	@Contribute private final BandwidthCounter _bandwidthCounter = new BandwidthCounterMock();
	private final ConnectionManager _connectionManager = my(ConnectionManager.class);
	
	@Test
	public void test() throws InterruptedException {
		final Contact contact = getContactMock();
		final ByteArraySocket socket = getByteArraySocketMock();
		_connectionManager.manageOutgoingSocket(contact, socket);
		_connectionManager.manageIncomingSocket(contact, socket);
		
		final ByRef<Thread> job = initComunications(contact);

		startAndWaitJobToFinish(job, 0);
		assertEquals(1, invokeCounter());
		assertEquals(_OUT_PACKET_SIZE, notifiedValue());
		
		startAndWaitJobToFinish(job, 1);
		assertEquals(2, invokeCounter());
		assertEquals(_IN_PACKET_SIZE, notifiedValue());
	}

	private void startAndWaitJobToFinish(ByRef<Thread> job, int index) throws InterruptedException {
		job.value = new Thread(findActor(index));
		job.value.start();
		sleepWhileJobIsAlive(job);
	}

	private void sleepWhileJobIsAlive(final ByRef<Thread> job) throws InterruptedException {
		while(job.value.isAlive()){ Thread.sleep(100); }
	}

	private Runnable findActor(int index) {
		return _threads.getActors().get(index);
	}

	private ByRef<Thread> initComunications(final Contact contact) {
		ByteConnection connection = _connectionManager.connectionFor(contact);
		ByRef<Thread> byRef = ByRef.newInstance();
		PacketScheduler sender = getPaketSchedulerMock(byRef);
		Consumer<byte[]> receiver = getCommunicationsReceiverMock(byRef);
		connection.initCommunications(sender, receiver);
		return byRef;
	}

	private int invokeCounter() {
		return ((BandwidthCounterMock)_bandwidthCounter).invokeCounter;
	}
	
	private int notifiedValue() {
		return ((BandwidthCounterMock)_bandwidthCounter).notifiedValue;
	}
	
	private Consumer<byte[]> getCommunicationsReceiverMock(	final ByRef<Thread> job1) {
		return new Consumer<byte[]>() { 
			@SuppressWarnings("deprecation") 
			@Override public void consume(byte[] value) {
				job1.value.stop();
			}
		};
	}

	private PacketScheduler getPaketSchedulerMock(final ByRef<Thread> job1) {
		return new PacketScheduler() {
			@Override public byte[] highestPriorityPacketToSend() { 
				return new byte[_OUT_PACKET_SIZE]; 
			}
			@SuppressWarnings("deprecation") 
			@Override public void previousPacketWasSent() { 
				job1.value.stop();
			}
		};
	}

	private ByteArraySocket getByteArraySocketMock() {
		return new ByteArraySocket(){
			@Override public byte[] read() {	return _BYTE_ARRAY_OK;	}
			@Override public void write(byte[] array) { /*ignore*/ }
			@Override public void crash() {/*ignore*/ }
		};
	}

	private Contact getContactMock() {
		return new Contact(){
			@Override public Signal<String> nickname() {
				return my(Signals.class).constant("Sandro");
			}
		};
	}
	
	private class BandwidthCounterMock implements BandwidthCounter{
		int invokeCounter = 0;
		int notifiedValue;
		
		private void store(int sizeBytes) {
			notifiedValue = sizeBytes;
			invokeCounter++;
		}
		@Override public void received(int sizeBytes) { store(sizeBytes); }
		@Override public void sent(int sizeBytes) {	store(sizeBytes); }
		@Override public Signal<Integer> downloadSpeed() { throw new sneer.commons.lang.exceptions.NotImplementedYet(); }
		@Override public Signal<Integer> uploadSpeed() {throw new sneer.commons.lang.exceptions.NotImplementedYet(); }
	};
}
