package spikes.lego.container;

import java.io.File;

import spikes.lego.container.impl.SimpleContainer;

public class ContainerUtils {
	
	private static Container container; 
	
	public static Container getContainer() {
		return getContainer(new File("lego.xml"));
	}

	public static Container getContainer(File configuration) {
		if(container != null) return container;
		container = new SimpleContainer();
		return container;
	}

}
