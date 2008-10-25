package sneer.skin.sound.speaker.tests;

import javax.sound.sampled.SourceDataLine;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import sneer.kernel.container.Inject;
import sneer.kernel.container.tests.TestThatIsInjected;
import sneer.pulp.clock.Clock;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.kernel.Audio;
import sneer.skin.sound.speaker.Speaker;
import wheel.lang.ImmutableByteArray;


@RunWith(JMock.class)
public class SpeakerTest extends TestThatIsInjected {
	
	private final Mockery mockery = new JUnit4Mockery();
	
	private final Audio audio = mockery.mock(Audio.class);
	private final SourceDataLine line = mockery.mock(SourceDataLine.class);

	@Inject private static Clock clock;
	@Inject private static KeyManager keyManager;
	@Inject private static TupleSpace tupleSpace;
	@Inject private static Speaker speaker;
	
	private final byte[] _pcm1 = new byte[] { 1, 2, 3, 5 };
	private final byte[] _pcm2 = new byte[] { 7, 11, 13, 17 };

	private int _packetSequence = 0;

	
	@Override
	protected Object[] getBindings() {
		return new Object[]{ audio };
	}


	@Test
	public void testSilentChannel() throws Exception {
		mockery.checking(new Expectations() {{
			one(audio).bestAvailableSourceDataLine(); will(returnValue(line));
			one(line).close();
		}});
		
		speaker.open();
		speaker.close();
	}

	
	@Test
	public void testOnlyTuplesFromContactsGetPlayed() throws Exception {
		mockery.checking(new SoundExpectations());

		speaker.open();
		contactPacket(_pcm1);
		contactPacket(_pcm2);

		myPacket(_pcm1);
	}

	
	@Test
	public void testTuplesPublishedAfterCloseAreNotPlayed() throws Exception {
		mockery.checking(new SoundExpectations() {{
			one(line).close();
		}});
		
		speaker.open();
		contactPacket(_pcm1);
		contactPacket(_pcm2);

		speaker.close();
		contactPacket(_pcm1);
	}

	
	@Ignore
	@Test
	public void testReceivingOutOfOrder() throws Exception {
		mockery.checking(new SoundExpectations());
		speaker.open();
		
		PcmSoundPacket p1 = pcmSoundPacketFor(contactKey(), _pcm1);
		PcmSoundPacket p2 = pcmSoundPacketFor(contactKey(), _pcm2);
		tupleSpace.acquire(p2);
		tupleSpace.acquire(p1);
	}
	

	class SoundExpectations extends Expectations {
		private final Sequence _mainSequence = mockery.sequence("main");
		
		public SoundExpectations() throws Exception {
			one(audio).bestAvailableSourceDataLine(); will(returnValue(line)); inMainSequence();

			one(line).isActive(); will(returnValue(false)); inMainSequence();
			one(line).open(); inMainSequence();
			one(line).start(); inMainSequence();
			allowing(line).isActive(); will(returnValue(true));

			one(line).write(_pcm1, 0, _pcm1.length); will(returnValue(_pcm1.length)); inMainSequence();
			one(line).write(_pcm2, 0, _pcm2.length); will(returnValue(_pcm2.length)); inMainSequence();
		}

		protected void inMainSequence() {
			inSequence(_mainSequence);
		}
	}

	
	@SuppressWarnings("deprecation")
	private PublicKey contactKey() {
		return keyManager.generateMickeyMouseKey("contact");
	}

	
	private void myPacket(byte[] pcm) {
		tupleSpace.acquire(pcmSoundPacketFor(keyManager.ownPublicKey(), pcm));
	}

	
	private void contactPacket(byte[] pcm) {
		tupleSpace.acquire(pcmSoundPacketFor(contactKey(), pcm));
	}
	
	
	private PcmSoundPacket pcmSoundPacketFor(PublicKey publicKey, final byte[] pcmPayload) {
		return new PcmSoundPacket(publicKey, clock.time(), new ImmutableByteArray(pcmPayload, pcmPayload.length), ++_packetSequence);
	}

}
