package snapps.listentome.speextuples.tests;

import static wheel.lang.Environments.my;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.junit.Test;

import snapps.listentome.speex.Decoder;
import snapps.listentome.speex.Encoder;
import snapps.listentome.speex.Speex;
import snapps.listentome.speextuples.SpeexPacket;
import snapps.listentome.speextuples.SpeexTuples;
import sneer.pulp.clock.Clock;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.tuples.Tuple;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.rooms.ActiveRoomKeeper;
import sneer.skin.sound.PcmSoundPacket;
import tests.Contribute;
import tests.TestInContainerEnvironment;
import wheel.lang.ByRef;
import wheel.lang.Consumer;
import wheel.lang.ImmutableByteArray;

public class SpeexTuplesTest extends TestInContainerEnvironment {
	
	private KeyManager _keyManager = my(KeyManager.class);
	private Clock _clock = my(Clock.class);
	private TupleSpace _tupleSpace = my(TupleSpace.class);
	@Contribute private final Speex _speex = mock(Speex.class);
	
	private final Encoder _encoder = mock(Encoder.class);
	private final Decoder _decoder = mock(Decoder.class);
	
	{
		checking(new Expectations() {{
			allowing(_speex).createEncoder(); will(returnValue(_encoder));
			allowing(_speex).createDecoder(); will(returnValue(_decoder));
		}});
	}
	
	private SpeexTuples _subject = my(SpeexTuples.class);
	
	@Test
	public void testPcmToSpeex() throws Exception {
		
		checking(new Expectations() {{ 
			final Sequence main = sequence("main");
			for (byte i=0; i<_subject.framesPerAudioPacket() * 2; i+=2) {
				one(_encoder).processData(new byte[] { i }); will(returnValue(false)); inSequence(main);
				one(_encoder).processData(new byte[] { (byte) (i + 1) });	will(returnValue(true)); inSequence(main);
				one(_encoder).getProcessedData();	will(returnValue(new byte[] { (byte) (i*42) }));
			}
		}});
		
		
		final ByRef<SpeexPacket> packet = ByRef.newInstance();
		_tupleSpace.addSubscription(SpeexPacket.class, new Consumer<SpeexPacket>() { @Override public void consume(SpeexPacket value) {
			assertNull(packet.value);
			packet.value = value;
		}});
		
		setRoom("MyChannel");
		for (byte[] frame : frames())
			_tupleSpace.acquire(myPacket(frame));
		
		assertNotNull(packet.value);
		assertFrames(packet.value.frames);
		assertEquals("MyChannel", packet.value.room);
	}
	
	@Test
	public void testSpeexToPcm() {
		
		final byte[][] speexPacketPayload = new byte[][] { {0} };
		final byte[] pcmPacketPayload = new byte[] { 17 };
		
		checking(new Expectations() {{ 
			one(_decoder).decode(speexPacketPayload);
				will(returnValue(new byte[][] { pcmPacketPayload }));
		}});
		
		setRoom("MyRoom");
		
		final ByRef<PcmSoundPacket> packet = ByRef.newInstance();
		_tupleSpace.addSubscription(PcmSoundPacket.class, new Consumer<PcmSoundPacket>() { @Override public void consume(PcmSoundPacket value) {
			assertNull(packet.value);
			packet.value = value;
		}});
		
		_tupleSpace.acquire(speexPacketFrom(contactKey(), speexPacketPayload, "MyRoom", (short)0));
		// tuples with ownPublicKey should be ignored
		_tupleSpace.acquire(speexPacketFrom(ownPublicKey(), speexPacketPayload, "MyRoom", (short)1));
			// tuples with different channel should be ignored
		_tupleSpace.acquire(speexPacketFrom(contactKey(), speexPacketPayload, "OtherRoom", (short)2));
		
		final PcmSoundPacket pcmPacket = packet.value;
		assertNotNull(pcmPacket);
		assertArrayEquals(pcmPacketPayload, pcmPacket.payload.copy());
		assertEquals(contactKey(), pcmPacket.publisher());
	}

	private void setRoom(String name) {
		my(ActiveRoomKeeper.class).setter().consume(name);
	}

	private Tuple speexPacketFrom(PublicKey contactKey, byte[][] bs, String channel, short sequence) {
		return new SpeexPacket(contactKey, bs, channel, sequence);
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
		return pcmSoundPacketFor(ownPublicKey(), pcm);
	}

	private PublicKey ownPublicKey() {
		return _keyManager.ownPublicKey();
	}
	
	private PcmSoundPacket pcmSoundPacketFor(PublicKey publicKey, final byte[] pcmPayload) {
		return new PcmSoundPacket(publicKey, _clock.time(), new ImmutableByteArray(pcmPayload, pcmPayload.length));
	}
	
	private byte[][] frames() {
		byte[][] frames = new byte[_subject.framesPerAudioPacket() * 2][];
		for (int i=0; i<frames.length; ++i)
			frames[i] = new byte[] { (byte) i };
		return frames;
	}

}
