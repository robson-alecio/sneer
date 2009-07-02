package sneer.bricks.skin.audio.speaker.tests;

import static sneer.foundation.environments.Environments.my;

import javax.sound.sampled.SourceDataLine;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.junit.Test;

import sneer.bricks.hardware.ram.arrays.ImmutableArrays;
import sneer.bricks.pulp.keymanager.Seals;
import sneer.bricks.pulp.tuples.TupleSpace;
import sneer.bricks.skin.audio.PcmSoundPacket;
import sneer.bricks.skin.audio.kernel.Audio;
import sneer.bricks.skin.audio.speaker.Speaker;
import sneer.foundation.brickness.Seal;
import sneer.foundation.brickness.testsupport.BrickTest;
import sneer.foundation.brickness.testsupport.Bind;

public class SpeakerTest extends BrickTest {
	
	private final Speaker _subject = my(Speaker.class);
	private final Seals _keyManager = my(Seals.class);
	private final TupleSpace _tupleSpace = my(TupleSpace.class);

	@Bind private final Audio _audio = mock(Audio.class);
	private final SourceDataLine _line = mock(SourceDataLine.class);
	
	@Test
	public void testSilentChannel() throws Exception {
		checking(new Expectations());
		
		_subject.open();
		_subject.close();
	}
	
	@Test (timeout = 3000)
	public void testOnlyTuplesFromContactsGetPlayed() throws Exception {
		checking(new SoundExpectations());

		_subject.open();
		_tupleSpace.acquire(p1());
		_tupleSpace.acquire(p2());

		_tupleSpace.acquire(myPacket(new byte[] {-1, 17, 0, 42}));

		_tupleSpace.waitForAllDispatchingToFinish();
		
		_subject.close();
	}
	
	@Test
	public void testTuplesPublishedAfterCloseAreNotPlayed() throws Exception {
		checking(new SoundExpectations());
		
		_subject.open();
		
		_tupleSpace.acquire(p1());
		_tupleSpace.acquire(p2());

		_tupleSpace.waitForAllDispatchingToFinish();

		_subject.close();
		_tupleSpace.acquire(p1());
		
		_tupleSpace.waitForAllDispatchingToFinish();
	}

	
	class SoundExpectations extends Expectations {
		private final Sequence _mainSequence = newSequence("main");
		
		SoundExpectations() throws Exception {
			one(_audio).tryToOpenPlaybackLine(); will(returnValue(_line)); inSequence(_mainSequence);
			one(_line).write(new byte[] { 1, 2, 3, 5 }, 0, 4); inSequence(_mainSequence);
			one(_line).write(new byte[] { 7, 11, 13, 17 }, 0, 4); inSequence(_mainSequence);
			one(_line).close(); inSequence(_mainSequence);
		}
	}


	private Seal contactKey() {
		return new Seal("anything".getBytes());
	}
	
	private PcmSoundPacket myPacket(byte[] pcm) {
		return pcmSoundPacketFor(_keyManager.ownSeal(), pcm);
	}

	private PcmSoundPacket contactPacket(byte[] pcm) {
		return pcmSoundPacketFor(contactKey(), pcm);
	}
	
	private PcmSoundPacket pcmSoundPacketFor(Seal publicKey, final byte[] pcmPayload) {
		return new PcmSoundPacket(publicKey, 0, my(ImmutableArrays.class).newImmutableByteArray(pcmPayload));
	}
	
	private PcmSoundPacket p1() {
		return contactPacket(new byte[] { 1, 2, 3, 5 });
	}
	
	private PcmSoundPacket p2() {
		return contactPacket(new byte[] { 7, 11, 13, 17 });
	}
}