package spikes.lego.app;

import spikes.lego.app.sample.SampleApplication;
import spikes.lego.container.Container;
import spikes.lego.container.ContainerUtils;

public class ApplicationRunner {

	
	public static void main(String[] args) throws Exception {
		Container container = ContainerUtils.getContainer(/* file */);
		SampleApplication application = container.create(SampleApplication.class);
		application.run();
	}
}
