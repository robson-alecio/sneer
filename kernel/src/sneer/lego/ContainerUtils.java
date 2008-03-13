package sneer.lego;

import sneer.lego.impl.SimpleContainer;

public class ContainerUtils {
	
	private static Container container; 
	
	public static Container getContainer() {
		return getContainer(null);
	}

	public static Container getContainer(Binder binder) {
		if(container == null) container = new SimpleContainer(binder);
		return container;
	}

    public static Container newContainer(Binder binder) {
        return new SimpleContainer(binder);
    }
	
}
