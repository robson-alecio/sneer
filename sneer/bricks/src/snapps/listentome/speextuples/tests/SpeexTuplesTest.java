package snapps.listentome.speextuples.tests;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import snapps.listentome.speex.Decoder;
import snapps.listentome.speex.Encoder;
import snapps.listentome.speex.Speex;
import snapps.listentome.speextuples.SpeexPacket;
import snapps.listentome.speextuples.SpeexTuples;
import sneer.kernel.container.Inject;
import sneer.pulp.clock.Clock;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.tuples.Tuple;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.sound.PcmSoundPacket;
import tests.Contribute;
import tests.JMockContainerEnvironment;
import tests.TestThatIsInjected;
import wheel.lang.ByRef;
import wheel.lang.Consumer;
import wheel.lang.ImmutableByteArray;

@RunWith(JMockContainerEnvironment.class)
public class SpeexTuplesTest extends TestThatIsInjected{
	
	@Inject
	private static KeyManager _keyManager;
	@Inject
	private static Clock _clock;
	@Inject
	private static TupleSpace _tupleSpace;

	@Inject
	private static SpeexTuples _subject;
	
	private final Mockery _mockery = new JUnit4Mockery();
	
	@Contribute
	private final Speex _speex = _mockery.mock(Speex.class);
	
	private final Encoder _encoder = _mockery.mock(Encoder.class);
	
	private final Decoder _decoder = _mockery.mock(Decoder.class);
	
	{
		_mockery.checking(new Expectations() {{
			allowing(_speex).createEncoder();
				will(returnValue(_encoder));
			allowing(_speex).createDecoder();
				will(returnValue(_decoder));
		}});
	}
	
	@Test
	public void testPcmToSpeex() throws Exception {
		
		_mockery.checking(new Expectations() {{ 
			final Sequence main = _mockery.sequence("main");
			for (byte i=0; i<_subject.framesPerAudioPacket() * 2; i+=2) {
				one(_encoder).processData(new byte[] { i });
					will(returnValue(false)); inSequence(main);
				one(_encoder).processData(new byte[] { (byte) (i + 1) });
					will(returnValue(true)); inSequence(main);
				one(_encoder).getProcessedData();
					will(returnValue(new byte[] { (byte) (i*42) }));
			}
		}});
		
		final ByRef<SpeexPacket> packet = ByRef.newInstance();
		_tupleSpace.addSubscription(SpeexPacket.class, new Consumer<SpeexPacket>() { @Override public void consume(SpeexPacket value) {
			assertNull(packet.value);
			packet.value = value;
		}});
		
		for (byte[] frame : frames())
			_tupleSpace.acquire(myPacket(frame));
		
		assertNotNull(packet.value);
		assertFrames(packet.value._frames);
	}
	
	@Test
	public void testSpeexToPcm() {
		
		final byte[][] speexPacketPayload = new byte[][] { {0} };
		final byte[] pcmPacketPayload = new byte[] { 17 };
		
		_mockery.checking(new Expectations() {{ 
			one(_decoder).decode(speexPacketPayload);
				will(returnValue(new byte[][] { pcmPacketPayload }));
		}});
		
		final ByRef<PcmSoundPacket> packet = ByRef.newInstance();
		_tupleSpace.addSubscription(PcmSoundPacket.class, new Consumer<PcmSoundPacket>() { @Override public void consume(PcmSoundPacket value) {
			assertNull(packet.value);
			packet.value = value;
		}});
		
		_tupleSpace.acquire(speexPacketFrom(contactKey(), speexPacketPayload));
		// tuples with ownPublicKey should be ignored
		_tupleSpace.acquire(speexPacketFrom(ownPublicKey(), speexPacketPayload));
		
		final PcmSoundPacket pcmPacket = packet.value;
		assertNotNull(pcmPacket);
		assertArrayEquals(pcmPacketPayload, pcmPacket.payload.copy());
		assertEquals(contactKey(), pcmPacket.publisher());
	}
	
	private Tuple speexPacketFrom(PublicKey contactKey, byte[][] bs) {
		return new SpeexPacket(contactKey, bs);
	}

	private void assertFrames(final byte[][] frames) {
		assertEquals(_subject.framesPerAudioPacket(), frames.length);
		int i = 0;
		for (byte[] frame : frames)  {
			assertArrayEquals(new byte[] { (byte) (i*42) }, frame);
			i += 2;
		}
	}
	
	@SuppressWarnings("deprecation")
	private PublicKey contactKey() {
		return _keyManager.generateMickeyMouseKey("contact");
	}
	
	private PcmSoundPacket myPacket(byte[] pcm) {
		return pcmSoundPacketFor(ownPublicKey(), pcm, (short)1);
	}

	private PublicKey ownPublicKey() {
		return _keyManager.ownPublicKey();
	}
	
	private PcmSoundPacket pcmSoundPacketFor(PublicKey publicKey, final byte[] pcmPayload, short sequence) {
		return new PcmSoundPacket(publicKey, _clock.time(), new ImmutableByteArray(pcmPayload, pcmPayload.length), sequence);
	}
	
	private byte[][] frames() {
		byte[][] frames = new byte[_subject.framesPerAudioPacket() * 2][];
		for (int i=0; i<frames.length; ++i)
			frames[i] = new byte[] { (byte) i };
		return frames;
	}

}
