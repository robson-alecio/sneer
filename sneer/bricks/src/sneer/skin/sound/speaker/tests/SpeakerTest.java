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

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.clock.Clock;
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.kernel.Audio;
import sneer.skin.sound.speaker.Speaker;
import wheel.lang.ImmutableByteArray;

@RunWith(JMock.class)
public class SpeakerTest  {
	
	private final Mockery mockery = new JUnit4Mockery();
	private final Audio audio = mockery.mock(Audio.class);
	private final Container container = ContainerUtils.newContainer(audio);
	private final KeyManager keyManager = container.produce(KeyManager.class);
	private final TupleSpace tupleSpace = container.produce(TupleSpace.class);
	private final Clock clock = container.produce(Clock.class);
	private final Speaker speaker = container.produce(Speaker.class);
	private final SourceDataLine sourceDataLine = mockery.mock(SourceDataLine.class);
	
	public class CommonExpectations extends Expectations {
		private final Sequence _mainSequence = mockery.sequence("main");
		
		{
			try {
				allowing(audio).bestAvailableSourceDataLine(); will(returnValue(sourceDataLine));
				
				allowing(sourceDataLine).isActive(); will(returnValue(true));
				one(sourceDataLine).open(); inMainSequence();
				one(sourceDataLine).start(); inMainSequence();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}

		protected void inMainSequence() {
			inSequence(_mainSequence);
		}
	}
	
	@Test
	public void testOnlyTuplesFromContactsGetPlayed() throws Exception {
		
		final byte[] pcmPayload1 = new byte[] { 1, 2, 3, 5 };
		final byte[] pcmPayload2 = new byte[] { 7, 11, 13, 17 };
		
		mockery.checking(new CommonExpectations() {{
			one(sourceDataLine).write(pcmPayload1, 0, pcmPayload1.length);
				will(returnValue(pcmPayload1.length)); inMainSequence();
			one(sourceDataLine).write(pcmPayload2, 0, pcmPayload2.length);
				will(returnValue(pcmPayload2.length)); inMainSequence();
		}});
		
		speaker.open();
		
		final PublicKey contactKey = generateContactKey();
		tupleSpace.acquire(pcmSoundPacketFor(contactKey, pcmPayload1));
		tupleSpace.acquire(pcmSoundPacketFor(contactKey, pcmPayload2));
		tupleSpace.acquire(pcmSoundPacketFor(ownPublickKey(), pcmPayload2));
		
		clock.advanceTime(1000);
	}
	
	@Test
	@Ignore
	public void testPlayingOrder() {
		final byte[] pcmPayload1 = new byte[] { 1, 2, 3, 5 };
		final byte[] pcmPayload2 = new byte[] { 7, 11, 13, 17 };
		
		mockery.checking(new CommonExpectations() {{
			one(sourceDataLine).write(pcmPayload1, 0, pcmPayload1.length);
				will(returnValue(pcmPayload1.length)); inMainSequence();
			one(sourceDataLine).write(pcmPayload2, 0, pcmPayload2.length);
				will(returnValue(pcmPayload2.length)); inMainSequence();
		}});
		
		speaker.open();
		
		final PublicKey contactKey = generateContactKey();
		final PcmSoundPacket packet1 = pcmSoundPacketFor(contactKey, pcmPayload1);
		final PcmSoundPacket packet2 = pcmSoundPacketFor(contactKey, pcmPayload2);
		tupleSpace.publish(packet2);
		tupleSpace.publish(packet1);
		
		clock.advanceTime(1000);
	}
	
	@Test
	public void testTuplesPublishedAfterCloseAreNotPlayed() {
		final byte[] pcmPayload = new byte[] { 1, 2, 3, 5 };
		
		mockery.checking(new CommonExpectations() {{
			one(sourceDataLine).write(pcmPayload, 0, pcmPayload.length);
				will(returnValue(pcmPayload.length)); inMainSequence();
			one(sourceDataLine).close();
		}});
		
		speaker.open();
		
		final PublicKey contactKey = generateContactKey();
		tupleSpace.acquire(pcmSoundPacketFor(contactKey, pcmPayload));
		
		clock.advanceTime(1000);
		
		speaker.close();
		tupleSpace.publish(pcmSoundPacketFor(contactKey, pcmPayload));
		
		clock.advanceTime(1000);
	}

	@SuppressWarnings("deprecation")
	private PublicKey generateContactKey() {
		return keyManager.generateMickeyMouseKey("contact");
	}

	private PublicKey ownPublickKey() {
		return keyManager.ownPublicKey();
	}

	private int _packetSequence = 0;
	
	private PcmSoundPacket pcmSoundPacketFor(PublicKey publicKey, final byte[] pcmPayload) {
		return new PcmSoundPacket(publicKey, clock.time(), new ImmutableByteArray(pcmPayload, pcmPayload.length), ++_packetSequence);
	}

}
