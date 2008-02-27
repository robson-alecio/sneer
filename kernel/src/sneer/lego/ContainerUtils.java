package sneer.lego;

import sneer.lego.impl.SimpleContainer;

public class ContainerUtils {
	
	private static Container container; 
	
	public static Container getContainer() {
		if(container == null) container = new SimpleContainer();
		return container;
	}

}
