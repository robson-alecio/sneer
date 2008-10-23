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
import sneer.pulp.keymanager.KeyManager;
import sneer.pulp.keymanager.PublicKey;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.kernel.Audio;
import sneer.skin.sound.speaker.Speaker;
import wheel.lang.ImmutableByteArray;

@RunWith(JMock.class)
public class SpeakerTest  {
	
	Mockery mockery = new JUnit4Mockery();
	
	@SuppressWarnings("deprecation")
	@Test
	public void test() throws Exception {
		
		final byte[] pcmPayload1 = new byte[] { 1, 2, 3, 5 };
		final byte[] pcmPayload2 = new byte[] { 7, 11, 13, 17 };
		final Audio audio = mockery.mock(Audio.class);
		final SourceDataLine sourceDataLine = mockery.mock(SourceDataLine.class);
		
		mockery.checking(new Expectations() {{
			allowing(audio).bestAvailableSourceDataLine(); will(returnValue(sourceDataLine));
			
			final Sequence main = mockery.sequence("main");
			allowing(sourceDataLine).isActive(); will(returnValue(true));
			one(sourceDataLine).open(); inSequence(main);
			one(sourceDataLine).start(); inSequence(main);
			one(sourceDataLine).write(pcmPayload1, 0, pcmPayload1.length);
				will(returnValue(pcmPayload1.length)); inSequence(main);
			one(sourceDataLine).write(pcmPayload2, 0, pcmPayload2.length);
				will(returnValue(pcmPayload2.length)); inSequence(main);
			one(sourceDataLine).close();
		}});
		
		final Container container = ContainerUtils.newContainer(audio);
		final KeyManager keyManager = container.produce(KeyManager.class);
		final TupleSpace tupleSpace = container.produce(TupleSpace.class);
		final Speaker speaker = container.produce(Speaker.class);
		speaker.open();
		
		final PublicKey contactKey = keyManager.generateMickeyMouseKey("contact");
		tupleSpace.publish(pcmSoundPacketFor(contactKey, pcmPayload1));
		tupleSpace.publish(pcmSoundPacketFor(contactKey, pcmPayload2));
		tupleSpace.publish(pcmSoundPacketFor(keyManager.ownPublicKey(), pcmPayload2));
		
		speaker.close();
		tupleSpace.publish(pcmSoundPacketFor(contactKey, pcmPayload1));
	}

	private PcmSoundPacket pcmSoundPacketFor(PublicKey publicKey, final byte[] pcmPayload) {
		final PcmSoundPacket packet = new PcmSoundPacket(publicKey, System.currentTimeMillis(), new ImmutableByteArray(pcmPayload, pcmPayload.length));
		return packet;
	}

}
