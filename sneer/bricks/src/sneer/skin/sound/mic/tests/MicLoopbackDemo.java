package sneer.skin.sound.mic.tests;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.kernel.impl.AudioCommon;
import sneer.skin.sound.mic.Mic;
import wheel.io.Logger;
import wheel.lang.Omnivore;
import wheel.lang.Threads;

public class MicLoopbackDemo {

	public static void main(String[] args) throws LineUnavailableException {
		Logger.redirectTo(System.out);
	
		Container container = ContainerUtils.newContainer();
		container.produce(Mic.class).open();
		
		final SourceDataLine line = AudioCommon.bestAvailableSourceDataLine();
		line.open();
		line.start();

		final byte[] buffer = new byte[320];
		
		TupleSpace tuples = container.produce(TupleSpace.class);
		tuples.addSubscription(PcmSoundPacket.class, new Omnivore<PcmSoundPacket>(){ @Override public void consume(PcmSoundPacket packet) {
			int length = packet._payload.copyTo(buffer);
			line.write(buffer, 0, length);
			System.out.println(length);
		}});

		Threads.sleepWithoutInterruptions(15000);
	}

}
