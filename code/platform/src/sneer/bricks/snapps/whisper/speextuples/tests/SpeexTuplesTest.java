package sneer.bricks.snapps.whisper.speextuples.tests;

import static sneer.foundation.environments.Environments.my;

import org.jmock.Expectations;
import org.junit.Test;

import sneer.bricks.hardware.clock.Clock;
import sneer.bricks.hardware.ram.arrays.ImmutableArrays;
import sneer.bricks.hardware.ram.arrays.ImmutableByteArray2D;
import sneer.bricks.pulp.keymanager.KeyManager;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.bricks.skin.audio.PcmSoundPacket;
import sneer.bricks.skin.rooms.ActiveRoomKeeper;
import sneer.bricks.snapps.whisper.speex.Decoder;
import sneer.bricks.snapps.whisper.speex.Encoder;
import sneer.bricks.snapps.whisper.speex.Speex;
import sneer.bricks.snapps.whisper.speextuples.SpeexPacket;
import sneer.bricks.snapps.whisper.speextuples.SpeexTuples;
import sneer.foundation.brickness.PublicKey;
import sneer.foundation.brickness.Tuple;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.brickness.testsupport.Bind;
import sneer.foundation.lang.ByRef;
import sneer.foundation.lang.Consumer;

public class SpeexTuplesTest extends BrickTest {
	
	private final KeyManager _keyManager = my(KeyManager.class);
	private final Clock _clock = my(Clock.class);
	private final TupleSpace _tupleSpace = my(TupleSpace.class);
	@Bind private final Speex _speex = mock(Speex.class);
	
	private final Encoder _encoder = mock(Encoder.class);
	private final Decoder _decoder = mock(Decoder.class);
	
	{
		checking(new Expectations() {{
			allowing(_speex).createEncoder(); will(returnValue(_encoder));
			allowing(_speex).createDecoder(); will(returnValue(_decoder));
		}});
	}
	
	private final SpeexTuples _subject = my(SpeexTuples.class);
	
	@Test (timeout = 4000)
	public void testPcmToSpeex() throws Exception {
		
		checking(new Expectations() {{ 
			for (byte i=0; i<_subject.framesPerAudioPacket() * 2; i+=2) {
				one(_encoder).processData(new byte[] { i }); will(returnValue(false));
				one(_encoder).processData(new byte[] { (byte) (i + 1) });	will(returnValue(true));
				one(_encoder).getProcessedData();	will(returnValue(new byte[] { (byte) (i*42) }));
			}
		}});
		
		
		final ByRef<SpeexPacket> packet = ByRef.newInstance();
		Consumer<SpeexPacket> refToAvoidGc = new Consumer<SpeexPacket>() { @Override public void consume(SpeexPacket value) {
			assertNull(packet.value);
			packet.value = value;
		}};
		_tupleSpace.addSubscription(SpeexPacket.class, refToAvoidGc);
		
		setRoom("MyChannel");
		for (byte[] frame : frames())
			_tupleSpace.acquire(myPacket(frame));
		
		_tupleSpace.waitForAllDispatchingToFinish();
		
		assertNotNull(packet.value);
		assertFrames(packet.value.frames.copy());
		assertEquals("MyChannel", packet.value.room);
	}
	
	
	@Test (timeout = 4000)
	public void testSpeexToPcm() {
		final byte[][] speexPacketPayload = new byte[][] { {0} };
		final byte[] pcmPacketPayload = new byte[] { 17 };
		
		checking(new Expectations() {{ 
			one(_decoder).decode(speexPacketPayload);
				will(returnValue(new byte[][] { pcmPacketPayload }));
		}});
		
		setRoom("MyRoom");
		
		final ByRef<PcmSoundPacket> packet = ByRef.newInstance();
		Consumer<PcmSoundPacket> refToAvoidGc = new Consumer<PcmSoundPacket>() { @Override public void consume(PcmSoundPacket value) {
			assertNull(packet.value);
			packet.value = value;
		}};
		_tupleSpace.addSubscription(PcmSoundPacket.class, refToAvoidGc);
		
		_tupleSpace.acquire(speexPacketFrom(contactKey(), speexPacketPayload, "MyRoom", (short)0));
		// tuples with ownPublicKey should be ignored
		_tupleSpace.acquire(speexPacketFrom(ownPublicKey(), speexPacketPayload, "MyRoom", (short)1));
			// tuples with different channel should be ignored
		_tupleSpace.acquire(speexPacketFrom(contactKey(), speexPacketPayload, "OtherRoom", (short)2));
		
		_tupleSpace.waitForAllDispatchingToFinish();
		final PcmSoundPacket pcmPacket = packet.value;
		assertNotNull(pcmPacket);
		assertArrayEquals(pcmPacketPayload, pcmPacket.payload.copy());
		assertEquals(contactKey(), pcmPacket.publisher());
	}

	private void setRoom(String name) {
		my(ActiveRoomKeeper.class).setter().consume(name);
	}

	private Tuple speexPacketFrom(PublicKey contactKey, byte[][] bs, String channel, short sequence) {
		return new SpeexPacket(contactKey, immutable(bs), channel, sequence);
	}

	private ImmutableByteArray2D immutable(byte[][] array2D) {
		return my(ImmutableArrays.class).newImmutableByteArray2D(array2D);
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
		return new PcmSoundPacket(publicKey, _clock.time(), my(ImmutableArrays.class).newImmutableByteArray(pcmPayload));
	}
	
	private byte[][] frames() {
		byte[][] frames = new byte[_subject.framesPerAudioPacket() * 2][];
		for (int i=0; i<frames.length; ++i)
			frames[i] = new byte[] { (byte) i };
		return frames;
	}

}
