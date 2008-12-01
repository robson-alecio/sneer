package sneer.skin.sound.mic.tests;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.mic.Mic;
import sneer.skin.sound.speaker.Speaker;
import wheel.io.Logger;
import wheel.lang.Consumer;
import wheel.lang.Threads;

public class SoundLoopbackDemo {

	public static void main(String[] args) {
		Logger.redirectTo(System.out);
	
		final Container container1 = ContainerUtils.newContainer();
		final TupleSpace tuples = container1.provide(TupleSpace.class);
		final Container container2 = ContainerUtils.newContainer(tuples);

		container2.provide(Mic.class).open();
		container1.provide(Speaker.class).open();
		
		tuples.addSubscription(PcmSoundPacket.class, new Consumer<PcmSoundPacket>() {
			@Override
			public void consume(PcmSoundPacket packet) {
				System.out.print("new byte[] {\n\t");
				int index = 0;
				for (byte b : packet.payload.copy()) {
					System.out.print(b +  ", ");
					if (++index == 10) {
						System.out.print("\n\t");
						index = 0;
					}
				}
				System.out.println("\n},");
			}
		});
		
		Threads.sleepWithoutInterruptions(10000);
	}
}
