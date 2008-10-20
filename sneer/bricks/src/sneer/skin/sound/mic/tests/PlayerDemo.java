package sneer.skin.sound.mic.tests;

import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;

import sneer.kernel.container.Container;
import sneer.kernel.container.ContainerUtils;
import sneer.pulp.tuples.TupleSpace;
import sneer.skin.sound.PcmSoundPacket;
import sneer.skin.sound.speaker.Speaker;
import wheel.io.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class PlayerDemo {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Logger.redirectTo(System.out);
	
		final Container container = ContainerUtils.newContainer();
		final TupleSpace tuples = container.produce(TupleSpace.class);
		final Speaker speaker = container.produce(Speaker.class);
		speaker.open();
		
		final XStream stream = new XStream(new DomDriver());
		final ObjectInputStream input = stream.createObjectInputStream(new FileReader("micdemo.xml"));
		try {
			while (true) {
				final PcmSoundPacket packet = (PcmSoundPacket) input.readObject();
				if (null == packet)
					break;
				tuples.publish(packet);
			}
		} finally {
			input.close();
		}
	}
}
