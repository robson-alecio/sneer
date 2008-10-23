package sneer.skin.sound.mic.tests;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.TargetDataLine;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.api.Invocation;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.action.CustomAction;
import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.clock.Clock;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.kernel.Audio;
import sneer.skin.sound.mic.Mic;
import wheel.lang.ByRef;
import wheel.lang.Omnivore;
import wheel.lang.Threads;

@RunWith(JMock.class)
public class MicTest {
	
	final Mockery mockery = new JUnit4Mockery();
	final Audio audio = mockery.mock(Audio.class);
	final Container container = ContainerUtils.newContainer(audio /*, threadPool */);
	final KeyManager keyManager = container.produce(KeyManager.class);
	final TupleSpace tupleSpace = container.produce(TupleSpace.class);
	final Clock clock = container.produce(Clock.class);
	final Mic mic = container.produce(Mic.class);
	final TargetDataLine targetDataLine = mockery.mock(TargetDataLine.class);
		
	@Test
	public void testPacketSequence() throws Exception {
		
		final Object monitor = new Object();
		final ByRef<Integer> sequence = ByRef.newInstance(0);
		final HashMap<Integer, PcmSoundPacket> seenPackets = new HashMap<Integer, PcmSoundPacket>();
		tupleSpace.addSubscription(PcmSoundPacket.class, new Omnivore<PcmSoundPacket>() {
			@Override
			public void consume(PcmSoundPacket value) {
				seenPackets.put(value._sequence, value);
			}
		});
		
		mockery.checking(new Expectations() {{
			allowing(audio).bestAvailableTargetDataLine();
				will(returnValue(targetDataLine));
			allowing(targetDataLine).open();
			allowing(targetDataLine).start();
			
			final Sequence main = mockery.sequence("main");
			allowing(targetDataLine).read(with(aNonNull(byte[].class)), with(0), with(320));
				will(new CustomAction("notify thread") { @Override public Object invoke(Invocation invocation) throws Throwable {
					if (++sequence.value > 2) {
						synchronized (monitor) {
							monitor.notify();
						}
					}
					final byte[] buffer = (byte[]) invocation.getParameter(0);
					writeInt(buffer, sequence.value);
					return 320;
				}}); inSequence(main);
				
			one(targetDataLine).close();
				inSequence(main);
		}});
		
		mic.open();
		
		synchronized (monitor) {
			monitor.wait();
		}
		
		mic.close();
		
		Threads.sleepWithoutInterruptions(200);
		
		assertEquals(sequence.value.intValue(), seenPackets.size());
		for (PcmSoundPacket packet : seenPackets.values()) {
			assertEquals(readInt(packet._payload.copy()), packet._sequence);
		}
	}

	private int readInt(byte[] buffer) throws IOException {
		return new DataInputStream(new ByteArrayInputStream(buffer)).readInt();
	}

	private void writeInt(final byte[] buffer, final int intValue)
			throws IOException {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		new DataOutputStream(bos).writeInt(intValue);
		System.arraycopy(bos.toByteArray(), 0, buffer, 0, bos.toByteArray().length);
	}
}
