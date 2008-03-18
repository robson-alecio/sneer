package sneer.lego;

import sneer.lego.impl.SimpleContainer;

public class ContainerUtils {
	
	private static Container container; 
	
	public static Container getContainer() {
		return getContainer(null, null);
	}

	public static Container getContainer(Binder binder, ConfigurationFactory configurationFactory) {
		if(container == null) container = new SimpleContainer(binder, configurationFactory);
		return container;
	}

    public static Container newContainer(Binder binder, ConfigurationFactory configurationFactory) {
        return new SimpleContainer(binder, configurationFactory);
    }
	
    public static void stopContainer() {
    	container = null;
    }
}
