package spikes.lego;

import spikes.legobricks.SampleApplication;

public class ToyRunner {

	
	public static void main(String[] args) throws Exception {
		Container container = ContainerUtils.getContainer(/* file */);
		SampleApplication application = container.create(SampleApplication.class);
		application.run();
	}
}
