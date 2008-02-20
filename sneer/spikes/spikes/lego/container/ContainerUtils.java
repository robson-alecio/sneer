package spikes.lego.container;

import spikes.lego.container.impl.SimpleContainer;

public class ContainerUtils {
	
	private static Container container; 
	
	public static Container getContainer() {
		if (container == null) container = new SimpleContainer();
		return container;
	}

}
