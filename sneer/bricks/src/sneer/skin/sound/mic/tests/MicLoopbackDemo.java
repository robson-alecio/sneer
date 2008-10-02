package sneer.skin.sound.mic.tests;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.mic.Mic;
import wheel.lang.Omnivore;

public class MicLoopbackDemo {

	public static void main(String[] args) {
		Container container = ContainerUtils.newContainer();
		container.produce(Mic.class).open();

		TupleSpace tuples = container.produce(TupleSpace.class);
		tuples.addSubscription(PcmSoundPacket.class, new Omnivore<PcmSoundPacket>(){ @Override public void consume(PcmSoundPacket packet) {
			System.out.println(packet);
		}});
		
	}

}
