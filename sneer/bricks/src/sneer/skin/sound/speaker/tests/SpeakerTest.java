package sneer.skin.sound.speaker.tests;

import javax.sound.sampled.SourceDataLine;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
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
	
	final Mockery mockery = new JUnit4Mockery();
	final Audio audio = mockery.mock(Audio.class);
	final Container container = ContainerUtils.newContainer(audio);
	final KeyManager keyManager = container.produce(KeyManager.class);
	final TupleSpace tupleSpace = container.produce(TupleSpace.class);
	final Clock clock = container.produce(Clock.class);
	final Speaker speaker = container.produce(Speaker.class);
	final SourceDataLine sourceDataLine = mockery.mock(SourceDataLine.class);
	
	public class CommonExpectations extends Expectations {{
		try {
			allowing(audio).bestAvailableSourceDataLine(); will(returnValue(sourceDataLine));
			
			final Sequence main = mainSequence();
			allowing(sourceDataLine).isActive(); will(returnValue(true));
			one(sourceDataLine).open(); inSequence(main);
			one(sourceDataLine).start(); inSequence(main);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	protected Sequence mainSequence() {
		return mockery.sequence("main");
	}}
	
	@Test
	public void testOnlyTuplesFromContactsGetPlayed() throws Exception {
		
		final byte[] pcmPayload1 = new byte[] { 1, 2, 3, 5 };
		final byte[] pcmPayload2 = new byte[] { 7, 11, 13, 17 };
		
		mockery.checking(new CommonExpectations() {{
			one(sourceDataLine).write(pcmPayload1, 0, pcmPayload1.length);
				will(returnValue(pcmPayload1.length)); inSequence(mainSequence());
			one(sourceDataLine).write(pcmPayload2, 0, pcmPayload2.length);
				will(returnValue(pcmPayload2.length)); inSequence(mainSequence());
		}});
		
		speaker.open();
		
		final PublicKey contactKey = generateContactKey();
		tupleSpace.publish(pcmSoundPacketFor(contactKey, pcmPayload1));
		tupleSpace.publish(pcmSoundPacketFor(contactKey, pcmPayload2));
		tupleSpace.publish(pcmSoundPacketFor(ownPublickKey(), pcmPayload2));
		
		clock.advanceTime(1000);
	}
	
	@Test
	public void testTuplesPublishedAfterCloseAreNotPlayed() {
		final byte[] pcmPayload = new byte[] { 1, 2, 3, 5 };
		
		mockery.checking(new CommonExpectations() {{
			one(sourceDataLine).write(pcmPayload, 0, pcmPayload.length);
				will(returnValue(pcmPayload.length)); inSequence(mainSequence());
			one(sourceDataLine).close();
		}});
		
		speaker.open();
		
		final PublicKey contactKey = generateContactKey();
		tupleSpace.publish(pcmSoundPacketFor(contactKey, pcmPayload));
		
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

	private PcmSoundPacket pcmSoundPacketFor(PublicKey publicKey, final byte[] pcmPayload) {
		return new PcmSoundPacket(publicKey, clock.time(), new ImmutableByteArray(pcmPayload, pcmPayload.length));
	}

}
