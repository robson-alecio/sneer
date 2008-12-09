package sneer.skin.sound.speaker.tests;

import javax.sound.sampled.SourceDataLine;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.kernel.container.Inject;
import sneer.pulp.clock.Clock;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.kernel.Audio;
import sneer.skin.sound.speaker.Speaker;
import tests.Contribute;
import tests.JMockContainerEnvironment;
import tests.TestThatIsInjected;
import wheel.lang.ImmutableByteArray;


@RunWith(JMockContainerEnvironment.class)
public class SpeakerTest extends TestThatIsInjected {
	
	@Inject private static Speaker _subject;
	@Inject private static Clock _clock;
	@Inject private static KeyManager _keyManager;
	@Inject private static TupleSpace _tupleSpace;

	private final Mockery _mockery = new JUnit4Mockery();
	@Contribute private final Audio _audio = _mockery.mock(Audio.class);
	private final SourceDataLine _line = _mockery.mock(SourceDataLine.class);
	
	
	@Test
	public void testSilentChannel() throws Exception {
		_mockery.checking(new Expectations());
		
		_subject.open();
		_subject.close();
	}
	
	@Test
	public void testOnlyTuplesFromContactsGetPlayed() throws Exception {
		_mockery.checking(new SoundExpectations());

		_subject.open();
		_tupleSpace.acquire(p1());
		_tupleSpace.acquire(p2());

		_tupleSpace.acquire(myPacket(new byte[] {-1, 17, 0, 42}));
		
		_subject.close();
	}
	
	@Test
	public void testTuplesPublishedAfterCloseAreNotPlayed() throws Exception {
		_mockery.checking(new SoundExpectations());
		
		_subject.open();
		
		_tupleSpace.acquire(p1());
		_tupleSpace.acquire(p2());

		_subject.close();
		_tupleSpace.acquire(p1());
	}

	
	class SoundExpectations extends Expectations {
		private final Sequence _mainSequence = _mockery.sequence("main");
		
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