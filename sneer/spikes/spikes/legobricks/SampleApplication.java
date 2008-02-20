package spikes.legobricks;

import spikes.lego.Container;
import spikes.lego.ContainerUtils;
import spikes.lego.Toy;

public class SampleApplication  {

	public static void main(String[] args) throws Exception {
		Container container = ContainerUtils.getContainer();
		Toy toy = container.create(NameGui.class);
		toy.run();
	}
}
