package sneer.skin.sound.speaker.tests;

import static sneer.brickness.Environments.my;

import javax.sound.sampled.SourceDataLine;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.junit.Test;

import sneer.brickness.PublicKey;
import sneer.brickness.testsupport.Contribute;
import sneer.brickness.testsupport.TestInBrickness;
import sneer.pulp.clock.Clock;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.kernel.Audio;
import sneer.skin.sound.speaker.Speaker;
import wheel.lang.ImmutableByteArray;

public class SpeakerTest extends TestInBrickness {
	
	private final Speaker _subject = my(Speaker.class);
	private final Clock _clock = my(Clock.class);
	private final KeyManager _keyManager = my(KeyManager.class);
	private final TupleSpace _tupleSpace = my(TupleSpace.class);

	@Contribute private final Audio _audio = mock(Audio.class);
	private final SourceDataLine _line = mock(SourceDataLine.class);
	
	
	@Test
	public void testSilentChannel() throws Exception {
		checking(new Expectations());
		
		_subject.open();
		_subject.close();
	}
	
	@Test
	public void testOnlyTuplesFromContactsGetPlayed() throws Exception {
		checking(new SoundExpectations());

		_subject.open();
		_tupleSpace.acquire(p1());
		_tupleSpace.acquire(p2());

		_tupleSpace.acquire(myPacket(new byte[] {-1, 17, 0, 42}));

		waitForTupleDispatch();
		
		_subject.close();
	}
	
	@Test
	public void testTuplesPublishedAfterCloseAreNotPlayed() throws Exception {
		checking(new SoundExpectations());
		
		_subject.open();
		
		_tupleSpace.acquire(p1());
		_tupleSpace.acquire(p2());

		waitForTupleDispatch();

		_subject.close();
		_tupleSpace.acquire(p1());
		
		waitForTupleDispatch();
	}

	
	class SoundExpectations extends Expectations {
		private final Sequence _mainSequence = sequence("main");
		
		SoundExpectations() throws Exception {
			one(_audio).tryToOpenPlaybackLine(); will(returnValue(_line)); inSequence(_mainSequence);
			one(_line).write(new byte[] { 1, 2, 3, 5 }, 0, 4); inSequence(_mainSequence);
			one(_line).write(new byte[] { 7, 11, 13, 17 }, 0, 4); inSequence(_mainSequence);
			one(_line).close(); inSequence(_mainSequence);
		}
	}


	@SuppressWarnings("deprecation")
	private PublicKey contactKey() {
		return _keyManager.generateMickeyMouseKey("contact");
	}
	
	private PcmSoundPacket myPacket(byte[] pcm) {
		return pcmSoundPacketFor(_keyManager.ownPublicKey(), pcm);
	}

	private PcmSoundPacket contactPacket(byte[] pcm) {
		return pcmSoundPacketFor(contactKey(), pcm);
	}
	
	private PcmSoundPacket pcmSoundPacketFor(PublicKey publicKey, final byte[] pcmPayload) {
		return new PcmSoundPacket(publicKey, _clock.time(), new ImmutableByteArray(pcmPayload, pcmPayload.length));
	}
	
	private PcmSoundPacket p1() {
		return contactPacket(new byte[] { 1, 2, 3, 5 });
	}
	
	private PcmSoundPacket p2() {
		return contactPacket(new byte[] { 7, 11, 13, 17 });
	}
}