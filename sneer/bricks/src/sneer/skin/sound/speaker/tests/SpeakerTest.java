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
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.kernel.Audio;
import sneer.skin.sound.speaker.Speaker;

@RunWith(JMock.class)
public class SpeakerTest  {
	
	Mockery mockery = new JUnit4Mockery();
	
	@Test
	public void test() throws Exception {
		
		final byte[] pcmPayload1 = new byte[] { 1, 2, 3, 5 };
		final byte[] pcmPayload2 = new byte[] { 7, 11, 13, 17 };
		final Audio audio = mockery.mock(Audio.class);
		final SourceDataLine sourceDataLine = mockery.mock(SourceDataLine.class);
		
		mockery.checking(new Expectations() {{
			allowing(audio).bestAvailableSourceDataLine(); will(returnValue(sourceDataLine));
			
			final Sequence main = mockery.sequence("main");
			
			one(sourceDataLine).open(); inSequence(main);
			one(sourceDataLine).start(); inSequence(main);
			one(sourceDataLine).write(pcmPayload1, 0, pcmPayload1.length);
				will(returnValue(pcmPayload1.length)); inSequence(main);
			one(sourceDataLine).write(pcmPayload2, 0, pcmPayload2.length);
				will(returnValue(pcmPayload2.length)); inSequence(main);
			one(sourceDataLine).close();
		}});
		
		final Container container = ContainerUtils.newContainer(audio);
		final TupleSpace tupleSpace = container.produce(TupleSpace.class);
		final Speaker speaker = container.produce(Speaker.class);
		speaker.open();
		
		tupleSpace.publish(pcmSoundPacketFor(pcmPayload1));
		tupleSpace.publish(pcmSoundPacketFor(pcmPayload2));
		
		speaker.close();
		tupleSpace.publish(pcmSoundPacketFor(pcmPayload1));
	}

	private PcmSoundPacket pcmSoundPacketFor(final byte[] pcmPayload) {
		return PcmSoundPacket.newInstance(pcmPayload, pcmPayload.length);
	}

}
