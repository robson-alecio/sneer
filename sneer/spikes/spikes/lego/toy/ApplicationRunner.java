package spikes.lego.toy;

import spikes.lego.app.Toy;
import spikes.lego.container.Container;
import spikes.lego.container.ContainerUtils;
import spikes.legobricks.name.SampleApplication;

public class ApplicationRunner {

	
	public static void main(String[] args) throws Exception {
		Container container = ContainerUtils.getContainer(/* file */);
		Toy application = container.create(SampleApplication.class);
		application.run();
	}
}
