package sneer.skin.sound.mic.tests;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.sound.mic.Mic;
import sneer.skin.sound.speaker.Speaker;
import wheel.io.Logger;
import wheel.lang.Threads;

public class SoundLoopbackDemo {

	public static void main(String[] args) {
		Logger.redirectTo(System.out);
	
		final Container container1 = ContainerUtils.newContainer();
		final TupleSpace tuples = container1.produce(TupleSpace.class);
		final Container container2 = ContainerUtils.newContainer(tuples);

		container2.produce(Mic.class).open();
		container1.produce(Speaker.class).open();
		
		Threads.sleepWithoutInterruptions(10000);
	}
}
